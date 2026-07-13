package com.lalit.order.controller;

import com.lalit.order.security.JwtService;
import com.lalit.order.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtService jwtService;

    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(
            Authentication authentication,
            @RequestBody CartItemRequest Cartrequest){

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();

        if(!cartService.addToCart(userId, Cartrequest)){
            return  ResponseEntity.badRequest().body("Product out of stock/User not found/User not found" );
        }

//        cartService.addToCart(userId , request);
        return ResponseEntity.status(HttpStatus.CREATED).build();

    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromCart(
            Authentication authentication,
            @PathVariable Long productId){

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();
        boolean deleted= cartService.deleteFromCart(userId,productId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
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
