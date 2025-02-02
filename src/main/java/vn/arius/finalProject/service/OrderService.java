package vn.arius.finalProject.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.Order;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Order handleCreate(String receiverName, String receiverAddress, String receiverPhone);

    ResultPaginationDTO fetchAll(Specification<Order> spec, Pageable pageable);

    Optional<Order> getOrderById(long id);

    boolean existsById(long id);

    Order handleUpdateStatus(Order order);

    List<Order> fetchAllOrder();
}
