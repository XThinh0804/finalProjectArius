package vn.arius.finalProject.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Role;

import java.util.List;

public interface RoleService {
    Role fetchById(long id);

    boolean existByName(String name);

    Role handleSaveRole(Role role);

    ResultPaginationDTO getRoles(Specification<Role> spec, Pageable pageable);

    void handleDelete(long id);

    Role handleUpdate(Role role);

    Role getRoleByName(String name);
}
