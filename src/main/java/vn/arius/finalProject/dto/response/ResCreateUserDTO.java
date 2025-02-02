package vn.arius.finalProject.dto.response;

import lombok.Data;
import vn.arius.finalProject.util.constant.GenderEnum;

import java.time.LocalDateTime;

@Data
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private String phone;
    private String avatar;
    private String address;
    private String gender;
    private int age;
    private LocalDateTime createAt;

}
