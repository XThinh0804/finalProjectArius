package vn.arius.finalProject.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.CartDetail;
import vn.arius.finalProject.entity.Order;
import vn.arius.finalProject.entity.OrderDetail;
import vn.arius.finalProject.entity.Product;
import vn.arius.finalProject.repository.OrderDetailRepository;
import vn.arius.finalProject.service.OrderDetailService;
import vn.arius.finalProject.service.ProductService;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final ProductService productService;

    public OrderDetailServiceImpl(OrderDetailRepository orderDetailRepository,ProductService productService) {
        this.orderDetailRepository = orderDetailRepository;
        this.productService = productService;
    }

    @Override
    public void handleCreateOrderDetail(Order order, List<CartDetail> cartDetails) {
        cartDetails.forEach(cartDetail -> {
            Product p = cartDetail.getProduct();
            p.setQuantity(p.getQuantity() - cartDetail.getQuantity());
            this.productService.handleUpdate(p,null);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(cartDetail.getQuantity());
            orderDetail.setPrice(cartDetail.getPrice());
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartDetail.getProduct());
            this.orderDetailRepository.save(orderDetail);
        });
    }

    @Override
    public List<OrderDetail> getAllByOrder(Order order) {
        List<OrderDetail> oDetail = this.orderDetailRepository.findByOrder(order);
        return oDetail;
    }
}
