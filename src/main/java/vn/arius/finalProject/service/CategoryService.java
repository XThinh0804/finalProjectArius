package vn.arius.finalProject.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.arius.finalProject.dto.response.ResCategoryDTO;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Category;

public interface CategoryService {
    Category handleSaveCategory(Category category);

    Category fetchById(long id);

    Category handleUpdate(Category category);

    void handleDelete(long id);

    ResultPaginationDTO fetchAllCategories(Specification<Category> spec, Pageable pageable);

    boolean existsByName(String name);

    ResCategoryDTO fetchCategoryDTO(long id);
}
