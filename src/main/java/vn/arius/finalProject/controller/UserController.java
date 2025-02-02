package vn.arius.finalProject.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.arius.finalProject.dto.response.ResCreateUserDTO;
import vn.arius.finalProject.dto.response.ResUpdateUserDTO;
import vn.arius.finalProject.dto.response.ResUserDTO;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.User;
import vn.arius.finalProject.service.EUserService;
import vn.arius.finalProject.service.UserService;
import vn.arius.finalProject.util.ExportUser;
import vn.arius.finalProject.util.annotation.ApiMessage;
import vn.arius.finalProject.util.error.InvalidException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class UserController {
    UserService userService;
    PasswordEncoder passwordEncoder;
    EUserService EUserService;

    @PostMapping(value = "/users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Create a new user")
    public ResponseEntity<ResCreateUserDTO> createUser(@Valid @RequestPart("user") User newUser,
                                                       @RequestPart(value = "avatar", required = false) MultipartFile avatar) throws InvalidException {
        boolean isEmailExist = this.userService.isEmailExist(newUser.getEmail());
        if (isEmailExist) {
            throw new InvalidException("Email " + newUser.getEmail() + " đã tồn tại, vui lòng sử dụng email khác!");
        }
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertToResCreateUserDTO(this.userService.createUser(newUser, avatar)));
    }

    @GetMapping("/users")
    @ApiMessage("fetch all users")
    public ResponseEntity<ResultPaginationDTO> getAllUser(
            @Filter Specification<User> spec,
            Pageable pageable) {

        return ResponseEntity.status(HttpStatus.OK).body(
                this.userService.fetchAllUser(spec, pageable));
    }

    @GetMapping("/users/{id}")
    @ApiMessage("fetch user by id")
    public ResponseEntity<ResUserDTO> getUserById(@PathVariable("id") long id) throws InvalidException {
        User fetchUser = this.userService.fetchUserById(id);
        if (fetchUser == null) {
            throw new InvalidException("User với id = " + id + " không tồn tại");
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(this.userService.convertToResUserDTO(fetchUser));
    }

    @DeleteMapping("/users/{id}")
    @ApiMessage("Delete a user")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") long id)
            throws InvalidException {
        User currentUser = this.userService.fetchUserById(id);
        if (currentUser == null) {
            throw new InvalidException("User với id = " + id + " không tồn tại");
        }

        this.userService.handleDeleteUser(id);
        return ResponseEntity.ok(null);
    }

    @PutMapping(value = "users", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Update a user")
    public ResponseEntity<ResUpdateUserDTO> updateUser(@Valid @RequestPart("user") User user,
                                                       @RequestPart(value = "avatar", required = false) MultipartFile avatar) throws InvalidException {
        User updateUser = this.userService.handleUpdateUser(user, avatar);
        if (updateUser == null) {
            throw new InvalidException("User với id = " + user.getId() + " không tồn tại");
        }
        return ResponseEntity.ok(this.userService.convertToResUpdateUserDTO(updateUser));
    }

    @GetMapping("users/export")
    @ApiMessage("Export all users success")
    public ResponseEntity<String> exportToExcel(HttpServletResponse response) throws IOException {
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=users_" + currentDateTime + ".xlsx";
        response.setHeader(headerKey, headerValue);

        List<User> listUsers = this.userService.fetchAllUser();

        ExportUser excelExporter = new ExportUser(listUsers);

        excelExporter.export(response);

        return ResponseEntity.ok().body("ok");
    }

    @PostMapping("users/import")
    @ApiMessage("Import users success")
    public ResponseEntity<?> importUserData(@RequestParam("file") MultipartFile file) {
        userService.handleSaveExel(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("users/search")
    public ResponseEntity<ResultPaginationDTO> searchUserByName(@RequestParam String query, Pageable pageable) {
        return ResponseEntity.ok().body(this.EUserService.searchByName(query, pageable));
    }

    @PostMapping("users/saveElastic")
    public void saveElastic() {
        this.EUserService.saveAll();
    }
}
