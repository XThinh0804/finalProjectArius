package vn.arius.finalProject.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Role;
import vn.arius.finalProject.service.RoleService;
import vn.arius.finalProject.util.annotation.ApiMessage;
import vn.arius.finalProject.util.error.InvalidException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RoleController {
    RoleService roleService;

    @PostMapping("/roles")
    @ApiMessage("Create a role")
    public ResponseEntity<Role> create(@Valid @RequestBody Role r) throws InvalidException {
        if (this.roleService.existByName(r.getName())) {
            throw new InvalidException("Role với name = " + r.getName() + " đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.handleSaveRole(r));
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch a role by id")
    public ResponseEntity<Role> getRoleById(@PathVariable("id") long id) throws InvalidException {
        Role role = this.roleService.fetchById(id);
        if (role == null) {
            throw new InvalidException("Role với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok().body(role);
    }

    @GetMapping("/roles")
    @ApiMessage("Fetch all roles")
    public ResponseEntity<ResultPaginationDTO> getAllRoles(
            @Filter Specification<Role> spec, Pageable pageable) {

        return ResponseEntity.ok(this.roleService.getRoles(spec, pageable));
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role r) throws InvalidException {
        if (this.roleService.fetchById(r.getId()) == null) {
            throw new InvalidException("Role với id = " + r.getId() + " không tồn tại");
        }
        return ResponseEntity.ok().body(this.roleService.handleUpdate(r));
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> deleteRole(@PathVariable("id") long id) throws InvalidException {
        if (this.roleService.fetchById(id) == null) {
            throw new InvalidException("Role với id = " + id + " không tồn tại");
        }
        this.roleService.handleDelete(id);
        return ResponseEntity.ok().body(null);
    }
}
