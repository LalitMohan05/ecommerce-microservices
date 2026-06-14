package com.lalit.order.service;

import com.lalit.order.client.ProductClient;
import com.lalit.order.dto.ProductResponse;
import lombok.RequiredArgsConstructor;
import com.lalit.order.dto.CartItemRequest;
import com.lalit.order.entity.CartItem;
//import com.lalit.order.entity.Product;
//import com.lalit.order.entity.User;
import com.lalit.order.repository.CartItemRepo;
//import com.lalit.order.repository.ProductRepo;
//import com.lalit.order.repository.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
//    private final ProductRepo productRepo;
//    private final UserRepo userRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductClient productClient;


    public boolean addToCart(Long userId, CartItemRequest request) {
//        Optional<Product> productOpt = productRepo.findById(request.getProductId());
//
//        if(productOpt.isEmpty()) return false;

//        Product product = productOpt.get();
//        if(product.getQuantity() < request.getQuantity()) return false;
//
//        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));
//
//        if(userOpt.isEmpty()) return false;
//
//        User user = userOpt.get();

        ProductResponse product = productClient.getProductById(request.getProductId());

        if(product.getQuantity() < request.getQuantity()){
            return false;
        }

        CartItem existingCartItem = cartItemRepo.findByUserIdAndProductId(userId, request.getProductId());
        if(existingCartItem != null){
            //cart item we are trying to add already exist in user cart
            //updating the quantity there

            existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
            existingCartItem.setPrice(product.getPrice());
            cartItemRepo.save(existingCartItem);

        } else{
            //Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setUserId(userId);
            cartItem.setProductId(request.getProductId());
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice());
            cartItemRepo.save(cartItem);
        }

        return  true;

    }

    public boolean deleteFromCart(Long userId, Long productId) {
//        Optional<Product> productOpt = productRepo.findById(productId);
//        Optional<User> userOpt = userRepo.findById(Long.valueOf(userId));

        CartItem cartItem=cartItemRepo.findByUserIdAndProductId(userId, productId);

        if(cartItem!=null){
            cartItemRepo.delete(cartItem);
            return true;

        }
        return false;
    }

    public List<CartItem> fetchCartItems(Long userId) {
//        return userRepo.findById(Long.valueOf(userId))
//                .map(cartItemRepo::findByUser)
//                .orElseGet(List::of);

        return cartItemRepo.findByUserId(userId);
    }

    public void clearCart(Long userId) {
//        userRepo.findById(Long.valueOf(userId)).ifPresent(
//                cartItemRepo::deleteByUser);

        cartItemRepo.deleteByUserId(userId);
    }
}
