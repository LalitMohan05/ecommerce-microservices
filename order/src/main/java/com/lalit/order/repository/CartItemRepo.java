package com.lalit.order.repository;

import com.lalit.order.entity.CartItem;
//import com.lalit.order.entity.Product;
//import com.lalit.order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem,Long> {
//    CartItem findByUserAndProduct(User user, Product product);
//
//    void deleteByUserAndProduct(User user, Product product);
//
//   // List<CartItem> findAllByUserId(Long user_id);
//
//    List<CartItem> findByUser(User user);
//
//
//    void deleteByUser(User user);

    CartItem findByUserIdAndProductId(Long userId, Long productId);

    void deleteByUserIdAndProductId(Long userId, Long productId);

    // List<CartItem> findAllByUserId(Long user_id);

    List<CartItem> findByUserId(Long userId);



    void deleteByUserId(Long userId);
}
