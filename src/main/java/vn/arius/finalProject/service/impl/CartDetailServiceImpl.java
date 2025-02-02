package vn.arius.finalProject.service.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.arius.finalProject.entity.Cart;
import vn.arius.finalProject.entity.CartDetail;
import vn.arius.finalProject.entity.OrderDetail;
import vn.arius.finalProject.entity.Product;
import vn.arius.finalProject.repository.CartDetailRepository;
import vn.arius.finalProject.service.CartDetailService;
import vn.arius.finalProject.service.CartService;
import vn.arius.finalProject.service.ProductService;

import java.util.List;
import java.util.Optional;

@Service
public class CartDetailServiceImpl implements CartDetailService {
    private final CartDetailRepository cartDetailRepository;
    private final ProductService productService;
    private final CartService cartService;

    public CartDetailServiceImpl(CartDetailRepository cartDetailRepository, ProductService productService, CartService cartService) {
        this.cartDetailRepository = cartDetailRepository;
        this.productService = productService;
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public CartDetail handleCreateCartDetail(long productId, long quantity) {
        Product product = this.productService.findByIdWithLock(productId);
        Cart cart = this.cartService.getCartByAuthenticatedUser();
        if(this.cartDetailRepository.existsByProductAndCart(product,cart)){
            CartDetail cartDetail = this.cartDetailRepository.findByProductAndCart(product,cart);
            if(product.getQuantity() < cartDetail.getQuantity() + quantity){
                throw new IllegalArgumentException("Số lượng sản phẩm "+ product.getName() + " vượt quá số lượng trong kho!");
            }
            cartDetail.setQuantity(cartDetail.getQuantity() + quantity);
            cartDetail.setPrice(product.getPrice()*cartDetail.getQuantity());
            this.cartDetailRepository.save(cartDetail);
            return cartDetail;
        }else{
        CartDetail cartDetail = new CartDetail();
            if(product.getQuantity() < quantity){
                throw new IllegalArgumentException("Số lượng sản phẩm "+ product.getName() + " vượt quá số lượng trong kho!");
            }
        cartDetail.setQuantity(quantity);
        cartDetail.setPrice(product.getPrice()*quantity);
        cart.setSum(cart.getSum() + 1);
        cartDetail.setProduct(product);
        cartDetail.setCart(cart);
        this.cartDetailRepository.save(cartDetail);
        return cartDetail;
        }
    }

    @Override
    public void deleteCartDetail(long id) {
        Optional<CartDetail>  cartDetail = this.cartDetailRepository.findById(id);
        Cart cart = this.cartService.getCartByAuthenticatedUser();
        cartDetail.get().getCart().setSum(cart.getSum() -1);
        this.cartDetailRepository.delete(cartDetail.get());
    }

    @Override
    public boolean existsById(long id) {
        return this.cartDetailRepository.existsById(id);
    }

    @Override
    public List<CartDetail> getCartDetailByAuthenticated() {
        Cart cart = this.cartService.getCartByAuthenticatedUser();
        List<CartDetail> cartDetails = this.cartDetailRepository.findByCart(cart);
        return  cartDetails;
    }

    @Override
    public CartDetail handleUpdateCartDetail(long id, long quantity) {
        Optional<CartDetail> cOptional = this.cartDetailRepository.findById(id);
        CartDetail cartDetail = cOptional.get();
        cartDetail.setQuantity(quantity);
        cartDetail.setPrice(cartDetail.getProduct().getPrice()*quantity);
        return this.cartDetailRepository.save(cartDetail);
    }

    @Override
    public void handleDeleteAll(List<CartDetail> cartDetails) {
        cartDetails.forEach(cartDetail -> {
            this.cartDetailRepository.delete(cartDetail);
        });
    }
}
