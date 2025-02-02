package vn.arius.finalProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import vn.arius.finalProject.entity.Cart;
import vn.arius.finalProject.entity.CartDetail;
import vn.arius.finalProject.entity.User;

import java.util.List;
@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {
    boolean existsByUser(User user);
    Cart findByUser(User user);
    List<Cart> findByCartDetailsIn(List<CartDetail> cartDetails);
}
