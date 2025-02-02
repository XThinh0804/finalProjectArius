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
import vn.arius.finalProject.dto.response.ResCategoryDTO;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Category;
import vn.arius.finalProject.service.CategoryService;
import vn.arius.finalProject.util.annotation.ApiMessage;
import vn.arius.finalProject.util.error.InvalidException;

@RestController
@RequestMapping("/api/v1")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CategoryController {
    CategoryService categoryService;

    @PostMapping("/categories")
    @ApiMessage("Create a category")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category c) throws InvalidException {
        if (this.categoryService.existsByName(c.getName())) {
            throw new InvalidException("Category với name = " + c.getName() + " đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.categoryService.handleSaveCategory(c));

    }

    @PutMapping("/categories")
    @ApiMessage("Update a category")
    public ResponseEntity<Category> updateCategory(@Valid @RequestBody Category c) throws InvalidException {
        if (this.categoryService.fetchById(c.getId()) == null) {
            throw new InvalidException("Category với id = " + c.getId() + " không tồn tại");
        }
        return ResponseEntity.ok().body(this.categoryService.handleUpdate(c));
    }

    @DeleteMapping("/categories/{id}")
    @ApiMessage("Delete a category")
    public ResponseEntity<Void> deleteCategory(@PathVariable("id") long id) throws InvalidException {
        if (this.categoryService.fetchById(id) == null) {
            throw new InvalidException("Category với id = " + id + " không tồn tại");
        }
        this.categoryService.handleDelete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/categories/{id}")
    @ApiMessage("Fetch a category by id")
    public ResponseEntity<Category> getCategoryById(@PathVariable("id") long id) throws InvalidException {
        Category category = this.categoryService.fetchById(id);
        if (category == null) {
            throw new InvalidException("Category với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok().body(category);
    }

    @GetMapping("/categories")
    @ApiMessage("Fetch all categories")
    public ResponseEntity<ResultPaginationDTO> getAllCategories(
            @Filter Specification<Category> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.categoryService.fetchAllCategories(spec, pageable));
    }

    @GetMapping("/categoriesdto/{id}")
    public ResponseEntity<ResCategoryDTO> getCategoryDTO(@PathVariable("id") long id) {
        return ResponseEntity.ok().body(this.categoryService.fetchCategoryDTO(id));
    }
}
