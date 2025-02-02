package vn.arius.finalProject.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Role;
import vn.arius.finalProject.entity.User;
import vn.arius.finalProject.entity.elasticsearch.EsRole;
import vn.arius.finalProject.entity.elasticsearch.UserDocument;
import vn.arius.finalProject.repository.UserDocumentRepository;
import vn.arius.finalProject.repository.UserRepository;
import vn.arius.finalProject.service.EUserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EUserServiceImpl implements EUserService {
    UserDocumentRepository userDocumentRepository;
    UserRepository userRepository;

    @Override
    public ResultPaginationDTO searchByName(String query, Pageable pageable) {
        Page<UserDocument> page = this.userDocumentRepository.searchByQuery(query, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();
        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(page.getTotalPages());
        mt.setTotal(page.getTotalElements());

        rs.setMeta(mt);
        List<UserDocument> listEUser = page.getContent();
        rs.setResult(listEUser);
        return rs;
    }

    @Override
    public void saveAll() {
        List<User> listUser = this.userRepository.findAll();
        List<UserDocument> listUserDocument = listUser.stream().map(item -> this.convertToUserDocument(item)).collect(Collectors.toList());
        this.userDocumentRepository.saveAll(listUserDocument);
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
}
