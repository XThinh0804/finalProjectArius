package vn.arius.finalProject.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.multipart.MultipartFile;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Product;

public interface ProductService {
    boolean existsByName(String name);
    Product handleCreateProduct(Product product, MultipartFile image);
    Product fetchProductById(long id);
    Product handleUpdate(Product product,MultipartFile image);
    void handleDelete(long id);
    ResultPaginationDTO fetchAllProducts(Specification<Product> spec, Pageable pageable);
    Product findByIdWithLock(long productId);
}
