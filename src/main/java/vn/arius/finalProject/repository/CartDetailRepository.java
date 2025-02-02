package vn.arius.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.arius.finalProject.entity.Cart;
import vn.arius.finalProject.entity.CartDetail;
import vn.arius.finalProject.entity.Product;

import java.util.List;
@Repository
public interface CartDetailRepository extends JpaRepository<CartDetail, Long> {
    boolean existsByProductAndCart(Product product, Cart cart);
    CartDetail findByProductAndCart(Product product,Cart cart);
    List<CartDetail> findByCart(Cart cart);
}
