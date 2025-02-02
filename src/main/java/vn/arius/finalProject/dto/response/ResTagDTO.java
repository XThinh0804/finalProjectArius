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
public class ResTagDTO {
    private long id;
    private String name;
    private List<ResProductDTO> productsDTO;
}
