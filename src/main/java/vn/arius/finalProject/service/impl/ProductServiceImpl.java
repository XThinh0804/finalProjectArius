package vn.arius.finalProject.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Cart;
import vn.arius.finalProject.entity.Category;
import vn.arius.finalProject.entity.Product;
import vn.arius.finalProject.entity.Tag;
import vn.arius.finalProject.repository.ProductRepository;
import vn.arius.finalProject.repository.TagRepository;
import vn.arius.finalProject.service.CartService;
import vn.arius.finalProject.service.CategoryService;
import vn.arius.finalProject.service.ProductService;
import vn.arius.finalProject.service.S3Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final S3Service s3Service;
    private final CartService cartService;
    private final TagRepository tagRepository;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService, S3Service s3Service,
                              CartService cartService, TagRepository tagRepository) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.s3Service = s3Service;
        this.cartService = cartService;
        this.tagRepository = tagRepository;
    }

    @Override
    public boolean existsByName(String name) {
        return this.productRepository.existsByName(name);
    }

    @Override
    public Product handleCreateProduct(Product product, MultipartFile image) {
        if (product.getTags() != null) {
            List<Long> reqTags = product.getTags().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Tag> dbTags = this.tagRepository.findByIdIn(reqTags);
            product.setTags(dbTags);
        }
        if (product.getCategory() != null) {
            Category c = this.categoryService.fetchById(product.getCategory().getId());
            product.setCategory(c);
        }
        if (image != null && !image.isEmpty()) {
            // save avatar
            String imageUrl = this.s3Service.uploadFile(image, "thinh/product");
            product.setImage(imageUrl);
        }
        return this.productRepository.save(product);
    }

    @Override
    public Product fetchProductById(long id) {
        Optional<Product> productOptional = this.productRepository.findById(id);
        if (productOptional.isPresent()) {
            return productOptional.get();
        }
        return null;
    }

    @Override
    public Product handleUpdate(Product product, MultipartFile image) {
        Product p = this.fetchProductById(product.getId());
        if (product.getTags() != null) {
            List<Long> reqTags = product.getTags().stream().map(x -> x.getId())
                    .collect(Collectors.toList());
            List<Tag> dbTags = this.tagRepository.findByIdIn(reqTags);
            p.setTags(dbTags);
        }
        p.setAuthor(product.getAuthor());
        p.setName(product.getName());
        p.setPublisher(product.getPublisher());
        p.setPrice(product.getPrice());
        p.setQuantity(product.getQuantity());
        p.setDescription(product.getDescription());
        if (image != null && !image.isEmpty()) {
            // save avatar
            String imageUrl = this.s3Service.uploadFile(image, "thinh/product");
            p.setImage(imageUrl);
        }
        if (product.getCategory() != null) {
            Category c = this.categoryService.fetchById(product.getCategory().getId());
            p.setCategory(c);
        }
        p = this.productRepository.save(p);
        return p;
    }

    @Override
    public void handleDelete(long id) {
        Product product = this.fetchProductById(id);
        List<Cart> carts = this.cartService.findByCartDetail(product.getCartDetails());
        carts.forEach(cart -> {
            cart.setSum(cart.getSum() - 1);
        });
        this.productRepository.deleteById(id);
    }

    @Override
    public ResultPaginationDTO fetchAllProducts(Specification<Product> spec, Pageable pageable) {
        Page<Product> pProduct = this.productRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pProduct.getTotalPages());
        mt.setTotal(pProduct.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pProduct.getContent());
        return rs;
    }

    @Override
    public Product findByIdWithLock(long productId) {
        return this.productRepository.findByIdWithLock(productId);
    }
}
