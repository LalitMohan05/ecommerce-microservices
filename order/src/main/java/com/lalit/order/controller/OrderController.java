package com.lalit.order.controller;

import com.lalit.order.dto.OrderResponse;
import com.lalit.order.dto.UpdateOrderStatusRequest;
import com.lalit.order.security.UserPrincipal;
import com.lalit.order.service.OrderService;
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
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Operation(
            summary = "Create order",
            description = "Creates an order using items currently present in the user's cart"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Cart is empty"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            Authentication authentication
    ) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(orderService.createOrder(userId));
    }

    @Operation(
            summary = "Update order status",
            description = "Updates order status (Admin only)"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid status"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest orderStatus
    ) {
        return ResponseEntity.ok(
                orderService.updateOrderStatus(
                        orderId,
                        orderStatus.getStatus()
                )
        );
    }

    @Operation(
            summary = "Get my orders",
            description = "Returns order history of authenticated user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Orders fetched successfully"
    )
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> createOrderHistory(
            Authentication authentication
    ) {

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();

        return ResponseEntity.ok(
                orderService.orderHistory(userId)
        );
    }
}