package vn.arius.finalProject.dto.response;

import lombok.Getter;
import lombok.Setter;
import vn.arius.finalProject.util.constant.GenderEnum;

import java.time.LocalDateTime;

@Getter
@Setter
public class ResUpdateUserDTO {
    private long id;
    private String name;
    private String gender;
    private String phone;
    private String address;
    private int age;
    private LocalDateTime updateAt;

}
