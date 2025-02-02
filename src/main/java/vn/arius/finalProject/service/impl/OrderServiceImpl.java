package vn.arius.finalProject.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.arius.finalProject.dto.response.ResultPaginationDTO;
import vn.arius.finalProject.entity.*;
import vn.arius.finalProject.repository.OrderRepository;
import vn.arius.finalProject.repository.ProductRepository;
import vn.arius.finalProject.service.*;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final CartService cartService;
    private final CartDetailService cartDetailService;
    private final ProductRepository productRepository;

    public OrderServiceImpl(OrderRepository orderRepository, OrderDetailService orderDetailService, CartService cartService,
                            CartDetailService cartDetailService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderDetailService = orderDetailService;
        this.cartService = cartService;
        this.cartDetailService = cartDetailService;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public Order handleCreate(String receiverName, String receiverAddress, String receiverPhone) {
        Order order = new Order();
        order.setReceiverName(receiverName);
        order.setReceiverAddress(receiverAddress);
        order.setReceiverPhone(receiverPhone);
        Cart cart = this.cartService.getCartByAuthenticatedUser();
        List<CartDetail> cartDetails = this.cartDetailService.getCartDetailByAuthenticated();
        cartDetails.forEach(cartDetail -> {
            Product product = this.productRepository.findByIdWithLock(cartDetail.getProduct().getId());
            if (product.getQuantity() < cartDetail.getQuantity()) {
                throw new IllegalArgumentException("Số lượng sản phẩm " + product.getName() + " không đủ trong kho!");
            }
        });
        double totalPrice = cartDetails.stream().mapToDouble(cartDetail -> cartDetail.getPrice()).sum();
        order.setTotaPrice(totalPrice);
        order.setUser(cart.getUser());
        this.orderRepository.save(order);
        cart.setSum(0);
        cart.setCartDetails(null);
        this.cartService.saveCart(cart);
        this.orderDetailService.handleCreateOrderDetail(order, cartDetails);
        this.cartDetailService.handleDeleteAll(cartDetails);
        return order;
    }

    @Override
    public ResultPaginationDTO fetchAll(Specification<Order> spec, Pageable pageable) {
        Page<Order> pageOrder = this.orderRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());

        mt.setPages(pageOrder.getTotalPages());
        mt.setTotal(pageOrder.getTotalElements());

        rs.setMeta(mt);

        rs.setResult(pageOrder.getContent());

        return rs;
    }

    @Override
    public Optional<Order> getOrderById(long id) {
        return this.orderRepository.findById(id);
    }

    @Override
    public boolean existsById(long id) {
        return this.orderRepository.existsById(id);
    }

    @Override
    public Order handleUpdateStatus(Order order) {
        Optional<Order> orderOptional = this.orderRepository.findById(order.getId());
        orderOptional.get().setStatus(order.getStatus());
        Order currentOrder = orderOptional.get();
        return this.orderRepository.save(currentOrder);
    }

    @Override
    public List<Order> fetchAllOrder() {
        return this.orderRepository.findAll();
    }
}
