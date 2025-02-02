package vn.arius.finalProject.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.arius.finalProject.entity.Cart;
import vn.arius.finalProject.service.CartService;
import vn.arius.finalProject.util.annotation.ApiMessage;
import vn.arius.finalProject.util.error.InvalidException;

@RestController
@RequestMapping("/api/v1")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CartController {
    CartService cartService;

    @GetMapping("/cart")
    @ApiMessage("fetch cart")
    public ResponseEntity<Cart> getCartByAuthenticatedUser() {
        Cart cart = this.cartService.getCartByAuthenticatedUser();
        return ResponseEntity.ok().body(cart);
    }

}
