package vn.arius.finalProject.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.arius.finalProject.dto.response.ResCategoryDTO;
import vn.arius.finalProject.dto.response.ResProductDTO;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Category;
import vn.arius.finalProject.entity.Product;
import vn.arius.finalProject.repository.CategoryRepository;
import vn.arius.finalProject.service.CategoryService;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category handleSaveCategory(Category category) {
        return this.categoryRepository.save(category);
    }

    @Override
    public Category fetchById(long id) {
        Optional<Category> cOptional = this.categoryRepository.findById(id);
        if (cOptional.isPresent()) {
            return cOptional.get();
        }
        return null;
    }

    @Override
    public Category handleUpdate(Category category) {
        Category c = this.fetchById(category.getId());
        c.setName(category.getName());
        c.setDescription(category.getDescription());
        return this.handleSaveCategory(c);
    }

    @Override
    public void handleDelete(long id) {
        this.categoryRepository.deleteById(id);
    }

    @Override
    public ResultPaginationDTO fetchAllCategories(Specification<Category> spec, Pageable pageable) {
        Page<Category> pCategory = this.categoryRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pCategory.getTotalPages());
        mt.setTotal(pCategory.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pCategory.getContent());
        return rs;
    }

    @Override
    public boolean existsByName(String name) {
        return this.categoryRepository.existsByName(name);
    }

    @Override
    public ResCategoryDTO fetchCategoryDTO(long id) {
        Category category = this.fetchById(id);
        ResCategoryDTO resCategoryDTO = this.convertToResCategoryDTO(category);
        return resCategoryDTO;
    }

    public ResCategoryDTO convertToResCategoryDTO(Category category) {
        ResCategoryDTO resCategoryDTO = new ResCategoryDTO();
        resCategoryDTO.setId(category.getId());
        resCategoryDTO.setName(category.getName());
        resCategoryDTO.setProductsDTO(category.getProducts().stream().map(item -> this.convertResProductDTO(item)).collect(Collectors.toList()));
        return resCategoryDTO;
    }

    public ResProductDTO convertResProductDTO(Product product) {
        ResProductDTO resProductDTO = new ResProductDTO();
        resProductDTO.setId(product.getId());
        resProductDTO.setName(product.getName());
        resProductDTO.setImage(product.getImage());
        resProductDTO.setPrice(product.getPrice());
        return resProductDTO;
    }
}
