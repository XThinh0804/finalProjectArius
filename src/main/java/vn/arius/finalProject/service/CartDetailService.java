package vn.arius.finalProject.service;

import vn.arius.finalProject.entity.CartDetail;

import java.util.List;

public interface CartDetailService {
    CartDetail handleCreateCartDetail(long productId, long quantity);
    void deleteCartDetail(long id);

    boolean existsById(long id);
    List<CartDetail> getCartDetailByAuthenticated();
    CartDetail handleUpdateCartDetail(long id, long quantity);
    void handleDeleteAll(List<CartDetail> cartDetails);

}
