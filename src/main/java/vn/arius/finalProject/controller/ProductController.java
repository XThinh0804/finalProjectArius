package vn.arius.finalProject.controller;

import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Product;
import vn.arius.finalProject.service.ProductService;
import vn.arius.finalProject.util.annotation.ApiMessage;
import vn.arius.finalProject.util.error.InvalidException;

@RestController
@RequestMapping("/api/v1")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ProductController {
    ProductService productService;

    @PostMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Create a product")
    public ResponseEntity<Product> create(@Valid @RequestPart("product") Product p,
                                          @RequestPart(value = "image", required = false) MultipartFile image) throws InvalidException {
        if (this.productService.existsByName(p.getName())) {
            throw new InvalidException("Product với name = " + p.getName() + " đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.productService.handleCreateProduct(p, image));
    }

    @PutMapping(value = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiMessage("Update a product")
    public ResponseEntity<Product> updateProduct(@Valid @RequestPart("product") Product p,
                                                 @RequestPart(value = "image", required = false) MultipartFile image) throws InvalidException {
        if (this.productService.fetchProductById(p.getId()) == null) {
            throw new InvalidException("Product với id = " + p.getId() + " không tồn tại");
        }
        return ResponseEntity.ok().body(this.productService.handleUpdate(p, image));
    }

    @DeleteMapping("/products/{id}")
    @ApiMessage("Delete a product by id")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") long id) throws InvalidException {
        if (this.productService.fetchProductById(id) == null) {
            throw new InvalidException("Product với id = " + id + " không tồn tại");
        }
        this.productService.handleDelete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/products/{id}")
    @ApiMessage("Get a product by id")
    public ResponseEntity<Product> fetchProductById(@PathVariable("id") long id) throws InvalidException {
        Product p = this.productService.fetchProductById(id);
        if (p == null) {
            throw new InvalidException("Product với id = " + id + " không tồn tại");
        }
        return ResponseEntity.ok().body(p);
    }

    @GetMapping("/products")
    @ApiMessage("Get all products")
    public ResponseEntity<ResultPaginationDTO> fetchAllProducts(@Filter Specification<Product> spec, Pageable pageable) {
        return ResponseEntity.ok().body(this.productService.fetchAllProducts(spec, pageable));
    }
}
