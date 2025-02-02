package vn.arius.finalProject.service.impl;

import org.springframework.stereotype.Service;
import vn.arius.finalProject.entity.Cart;
import vn.arius.finalProject.entity.CartDetail;
import vn.arius.finalProject.entity.User;
import vn.arius.finalProject.repository.CartRepository;
import vn.arius.finalProject.service.CartService;
import vn.arius.finalProject.service.UserService;
import vn.arius.finalProject.util.SecurityUtil;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserService userService;

    public CartServiceImpl(CartRepository cartRepository,UserService userService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
    }

    @Override
    public void saveCart(Cart cart) {
        this.cartRepository.save(cart);
    }

    @Override
    public boolean existsByUser(User user) {
        return this.cartRepository.existsByUser(user);
    }
    @Override
    public void createCart(User user) {
        Cart cart = new Cart();
        cart.setSum(0);
        cart.setUser(user);
        this.cartRepository.save(cart);
    }

    @Override
    public Cart getCartByAuthenticatedUser() {
        if(SecurityUtil.getCurrentUserLogin().isPresent()){
            User user = this.userService.handleGetUserByUsername(SecurityUtil.getCurrentUserLogin().get());
            Cart cart = this.cartRepository.findByUser(user);
            return cart;
        }
        return null;
    }

    @Override
    public List<Cart> findByCartDetail(List<CartDetail> cartDetails) {
        List<Cart> carts = this.cartRepository.findByCartDetailsIn(cartDetails);
        return carts;
    }
}
