package vn.arius.finalProject.service;

import vn.arius.finalProject.entity.CartDetail;
import vn.arius.finalProject.entity.Order;
import vn.arius.finalProject.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    void handleCreateOrderDetail(Order order , List<CartDetail> cartDetails);
    List<OrderDetail> getAllByOrder(Order order);

}
