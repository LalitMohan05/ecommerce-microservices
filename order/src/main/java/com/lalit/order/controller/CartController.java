package com.lalit.order.controller;

import lombok.RequiredArgsConstructor;
import com.lalit.order.dto.CartItemRequest;
import com.lalit.order.service.CartService;
import com.lalit.order.entity.CartItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(
            @RequestHeader("X-User-ID") Long userId,
            @RequestBody CartItemRequest request){

        if(!cartService.addToCart(userId, request)){
            return  ResponseEntity.badRequest().body("Product out of stock/User not found/User not found" );
        }

//        cartService.addToCart(userId , request);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromCart(
            @RequestHeader("X-User-ID") Long userId,
            @PathVariable Long productId){
        boolean deleted= cartService.deleteFromCart(userId,productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @GetMapping("/allCart")
    public ResponseEntity<List<CartItem>> allCartItems(
            @RequestHeader("X-User-ID") Long userId){

        return ResponseEntity.ok(cartService.fetchCartItems(userId));
    }
}
