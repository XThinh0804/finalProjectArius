package vn.arius.finalProject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ResCategoryDTO {
    private long id;
    private String name;
    List<ResProductDTO> productsDTO;
}
