package vn.arius.finalProject.controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.arius.finalProject.entity.Order;
import vn.arius.finalProject.entity.OrderDetail;
import vn.arius.finalProject.service.OrderDetailService;
import vn.arius.finalProject.service.OrderService;
import vn.arius.finalProject.util.annotation.ApiMessage;
import vn.arius.finalProject.util.error.InvalidException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class OrderDetailController {
    OrderDetailService orderDetailService;
    OrderService orderService;

    @GetMapping("/orderDetail/{id}")
    @ApiMessage("fetch order detail by order")
    public ResponseEntity<List<OrderDetail>> getOrderDetailByOrderId(@PathVariable("id") long id) throws InvalidException {

        Optional<Order> order = this.orderService.getOrderById(id);
        if (!order.isPresent()) {
            throw new InvalidException("Order không tồn tại!");
        }
        return ResponseEntity.ok().body(this.orderDetailService.getAllByOrder(order.get()));
    }
}
