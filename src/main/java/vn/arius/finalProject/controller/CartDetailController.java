package vn.arius.finalProject.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.arius.finalProject.entity.CartDetail;
import vn.arius.finalProject.service.CartDetailService;
import vn.arius.finalProject.util.annotation.ApiMessage;
import vn.arius.finalProject.util.error.InvalidException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class CartDetailController {
    CartDetailService cartDetailService;

    @PostMapping("/cartDetail")
    @ApiMessage("create cart detail")
    public ResponseEntity<CartDetail> createCartDetail(@RequestParam("productId") long productId,
                                                       @RequestParam("quantity") long quantity) {
        return ResponseEntity.ok().body(this.cartDetailService.handleCreateCartDetail(productId, quantity));

    }

    @DeleteMapping("/cartDetail/{id}")
    @ApiMessage("Delete a cart detail")
    public ResponseEntity<Void> deleteCartDetail(@PathVariable("id") long id) throws InvalidException {
        if (!this.cartDetailService.existsById(id)) {
            throw new InvalidException("Không tìm thấy cart detail cần xóa!");
        } else {
            this.cartDetailService.deleteCartDetail(id);
        }
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/cartDetail")
    @ApiMessage("fetch cart detail by cart")
    public ResponseEntity<List<CartDetail>> getCartDetailByCart() {
        return ResponseEntity.ok().body(this.cartDetailService.getCartDetailByAuthenticated());
    }

    @PutMapping("/cartDetail/{id}")
    @ApiMessage("update cart detail")
    public ResponseEntity<CartDetail> updateCartDetail(@PathVariable("id") long id,
                                                       @RequestParam("quantity") long quantity) {
        return ResponseEntity.ok().body(this.cartDetailService.handleUpdateCartDetail(id, quantity));
    }
}
