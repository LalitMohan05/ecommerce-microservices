package com.lalit.order.service;

import com.lalit.order.client.ProductClient;
import com.lalit.order.dto.ProductResponse;
import com.lalit.order.exceptions.CartItemNotFoundException;
import com.lalit.order.exceptions.InsufficientStockException;
import lombok.RequiredArgsConstructor;
import com.lalit.order.dto.CartItemRequest;
import com.lalit.order.entity.CartItem;
import com.lalit.order.repository.CartItemRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartService {
    private final CartItemRepo cartItemRepo;
    private final ProductClient productClient;


    public void addToCart(Long userId, CartItemRequest request) {

        ProductResponse product = productClient.getProductById(request.getProductId());

        if(product.getQuantity() < request.getQuantity()){
            throw new InsufficientStockException("Only "+product.getQuantity() +" units available for product: "+
                    product.getName()) ;
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

    }

    public void deleteFromCart(Long userId, Long productId) {
        CartItem cartItem=cartItemRepo.findByUserIdAndProductId(userId, productId);

        if(cartItem!=null){
            cartItemRepo.delete(cartItem);
        }
        throw new CartItemNotFoundException(
                "Product with id "
                +productId
                +"not found in cart"
        );
    }

    public List<CartItem> fetchCartItems(Long userId) {
        return cartItemRepo.findByUserId(userId);
    }

    public void clearCart(Long userId) {
        cartItemRepo.deleteByUserId(userId);
    }
}
