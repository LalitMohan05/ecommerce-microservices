package com.lalit.order.controller;

import com.lalit.order.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import com.lalit.order.dto.CartItemRequest;
import com.lalit.order.service.CartService;
import com.lalit.order.entity.CartItem;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(
            Authentication authentication,
            @RequestBody CartItemRequest CartRequest){

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();

        cartService.addToCart(userId, CartRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromCart(
            Authentication authentication,
            @PathVariable Long productId){

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();
        cartService.deleteFromCart(userId,productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/allCart")
    public ResponseEntity<List<CartItem>> allCartItems(
            Authentication authentication){
        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();

        return ResponseEntity.ok(cartService.fetchCartItems(userId));
    }
}
