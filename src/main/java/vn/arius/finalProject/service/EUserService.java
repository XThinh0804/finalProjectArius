package vn.arius.finalProject.service;

import org.springframework.data.domain.Pageable;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;

public interface EUserService {
    ResultPaginationDTO searchByName(String query, Pageable pageable);

    void saveAll();
}
