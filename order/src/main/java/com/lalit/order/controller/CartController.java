package com.lalit.order.controller;

import com.lalit.order.dto.CartItemRequest;
import com.lalit.order.entity.CartItem;
import com.lalit.order.security.UserPrincipal;
import com.lalit.order.service.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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

    @Operation(
            summary = "Add product to cart",
            description = "Adds a product to the authenticated user's cart"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Item added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or insufficient stock"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/addToCart")
    public ResponseEntity<String> addToCart(
            Authentication authentication,
            @Valid @RequestBody CartItemRequest cartRequest
    ) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();

        cartService.addToCart(userId, cartRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Product added to cart successfully");
    }

    @Operation(
            summary = "Remove product from cart",
            description = "Removes a product from the authenticated user's cart"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Item removed successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromCart(
            Authentication authentication,
            @PathVariable Long productId
    ) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();

        cartService.deleteFromCart(userId, productId);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get cart items",
            description = "Returns all cart items of the authenticated user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Cart items fetched successfully"
    )
    @GetMapping("/allCart")
    public ResponseEntity<List<CartItem>> allCartItems(
            Authentication authentication
    ) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();

        return ResponseEntity.ok(
                cartService.fetchCartItems(userId)
        );
    }
}