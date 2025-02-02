package vn.arius.finalProject.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import vn.arius.finalProject.dto.response.ResCreateUserDTO;
import vn.arius.finalProject.dto.response.ResUpdateUserDTO;
import vn.arius.finalProject.dto.response.ResUserDTO;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.User;

import java.util.List;

public interface UserService {
    User createUser(User user, MultipartFile avatar);

    boolean isEmailExist(String email);

    ResCreateUserDTO convertToResCreateUserDTO(User user);

    ResultPaginationDTO fetchAllUser(Specification<User> spec, Pageable pageable);

    List<User> fetchAllUser();

    User fetchUserById(long id);

    ResUserDTO convertToResUserDTO(User user);

    void handleDeleteUser(long id);

    User handleUpdateUser(User user, MultipartFile avatar);

    ResUpdateUserDTO convertToResUpdateUserDTO(User user);

    User handleGetUserByUsername(String username);

    void updateUserToken(String token, String email);

    User getUserByRefreshTokenAndEmail(String token, String email);

    User getUserByUsername(String username);

    void handleSaveExel(MultipartFile file);
}
