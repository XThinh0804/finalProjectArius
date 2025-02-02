package vn.arius.finalProject.service;

import vn.arius.finalProject.entity.Cart;
import vn.arius.finalProject.entity.CartDetail;
import vn.arius.finalProject.entity.User;

import java.util.List;

public interface CartService {
    void saveCart(Cart cart);
    boolean existsByUser(User user);
    void createCart(User user);
    Cart getCartByAuthenticatedUser();
    List<Cart> findByCartDetail(List<CartDetail> cartDetails);
}
