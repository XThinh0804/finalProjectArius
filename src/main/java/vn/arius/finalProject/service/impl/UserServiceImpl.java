package vn.arius.finalProject.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.arius.finalProject.dto.response.ResCreateUserDTO;
import vn.arius.finalProject.dto.response.ResUpdateUserDTO;
import vn.arius.finalProject.dto.response.ResUserDTO;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Cart;
import vn.arius.finalProject.entity.Role;
import vn.arius.finalProject.entity.User;
import vn.arius.finalProject.entity.elasticsearch.EsRole;
import vn.arius.finalProject.entity.elasticsearch.UserDocument;
import vn.arius.finalProject.repository.CartRepository;
import vn.arius.finalProject.repository.UserDocumentRepository;
import vn.arius.finalProject.repository.UserRepository;
import vn.arius.finalProject.service.RoleService;
import vn.arius.finalProject.service.S3Service;
import vn.arius.finalProject.service.UserService;
import vn.arius.finalProject.util.ImportUser;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CartRepository cartRepository;
    private final S3Service s3Service;
    private final PasswordEncoder passwordEncoder;

    private final UserDocumentRepository userDocumentRepository;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService, CartRepository cartRepository, S3Service s3Service, PasswordEncoder passwordEncoder, UserDocumentRepository userDocumentRepository) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.cartRepository = cartRepository;
        this.s3Service = s3Service;
        this.passwordEncoder = passwordEncoder;
        this.userDocumentRepository = userDocumentRepository;
    }

    @Override
    public User createUser(User user, MultipartFile avatar) {
        if (user.getRole() != null) {
            Role r = this.roleService.fetchById(user.getRole().getId());
            user.setRole(r != null ? r : null);
        } else {
            Role role = this.roleService.fetchById(2);
            user.setRole(role);
        }
        if (avatar != null && !avatar.isEmpty()) {
            // save avatar
            String avatarUrl = this.s3Service.uploadFile(avatar, "thinh/avatar");
            user.setAvatar(avatarUrl);
        }
        Cart cart = new Cart();
        this.userDocumentRepository.save((this.convertToUserDocument(user)));
        UserDocument userDocument = this.convertToUserDocument(user);
        System.out.println(userDocument.getEmail());
        User currentUser = this.userRepository.save(user);
        cart.setUser(currentUser);
        this.cartRepository.save(cart);
        return currentUser;
    }

    private UserDocument convertToUserDocument(User user) {
        UserDocument userDocument = new UserDocument();
        userDocument.setId(String.valueOf(user.getId()));
        userDocument.setName(user.getName());
        userDocument.setEmail(user.getEmail());
        userDocument.setAddress(user.getAddress());
        userDocument.setPhone(user.getPhone());
        userDocument.setRole(this.convertToEsRole(user.getRole()));
        return userDocument;
    }

    private EsRole convertToEsRole(Role role) {
        EsRole esRole = new EsRole();
        esRole.setId(String.valueOf(role.getId()));
        esRole.setName(role.getName());
        return esRole;
    }

    @Override
    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    @Override
    public ResCreateUserDTO convertToResCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setPhone(user.getPhone());
        res.setCreateAt(user.getCreateAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    @Override
    public ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable) {
        Page<User> pageUser = this.userRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageUser.getTotalPages());
        mt.setTotal(pageUser.getTotalElements());

        rs.setMeta(mt);

        // remove sensitive data
        List<ResUserDTO> listUser = pageUser.getContent()
                .stream().map(item -> this.convertToResUserDTO(item))
                .collect(Collectors.toList());

        rs.setResult(listUser);

        return rs;
    }

    @Override
    public List<User> fetchAllUser() {
        List<User> listUser = this.userRepository.findAll();
        return listUser;
    }

    @Override
    public User fetchUserById(long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    public ResUserDTO convertToResUserDTO(User user) {
        ResUserDTO res = new ResUserDTO();
        ResUserDTO.RoleUser roleUser = new ResUserDTO.RoleUser();
        if (user.getRole() != null) {
            roleUser.setId(user.getRole().getId());
            roleUser.setName(user.getRole().getName());
            res.setRole(roleUser);
        }
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setAvatar(user.getAvatar());
        res.setName(user.getName());
        res.setPhone(user.getPhone());
        res.setAge(user.getAge());
        res.setUpdateAt(user.getUpdateAt());
        res.setCreateAt(user.getCreateAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    @Override
    public void handleDeleteUser(long id) {
//        String avatar = user.get().getAvatar();
//        if(avatar != null){
//            this.s3Service.deleteAvatarFile(avatar);
//        }
        this.userRepository.deleteById(id);
    }

    @Override
    public User handleUpdateUser(User user, MultipartFile avatar) {
        User currentUser = this.fetchUserById(user.getId());
        if (currentUser != null) {
            if (avatar != null && !avatar.isEmpty()) {
                // save avatar
                String avatarUrl = this.s3Service.uploadFile(avatar, "thinh/avatar");
                currentUser.setAvatar(avatarUrl);
            }
            currentUser.setAddress(user.getAddress());
            currentUser.setGender(user.getGender());
            currentUser.setPhone(user.getPhone());
            currentUser.setAge(user.getAge());
            currentUser.setName(user.getName());

            // check role
            if (user.getRole() != null) {
                Role r = this.roleService.fetchById(user.getRole().getId());
                currentUser.setRole(r != null ? r : null);
            }

            // update
            currentUser = this.userRepository.save(currentUser);
        }
        return currentUser;
    }

    @Override
    public ResUpdateUserDTO convertToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setName(user.getName());
        res.setAge(user.getAge());
        res.setPhone(user.getPhone());
        res.setUpdateAt(user.getUpdateAt());
        res.setGender(user.getGender());
        res.setAddress(user.getAddress());
        return res;
    }

    @Override
    public User handleGetUserByUsername(String username) {
        Optional<User> userOptional = this.userRepository.findByEmail(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        return null;
    }

    @Override
    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    @Override
    public User getUserByRefreshTokenAndEmail(String token, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(token, email);
    }

    @Override
    public User getUserByUsername(String username) {
        return this.userRepository.findByEmail(username).get();
    }

    @Override
    public void handleSaveExel(MultipartFile file) {
        if (ImportUser.isValidExcelFile(file)) {
            try {

                List<User> users = ImportUser.getUserDataFrom(file.getInputStream());
                users.stream().forEach(user -> {
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    Role role = this.roleService.fetchById(2);
                    user.setRole(role);
                    userRepository.save(user);
                });

            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }
    }

}