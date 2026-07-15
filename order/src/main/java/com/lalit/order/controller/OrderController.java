package com.lalit.order.controller;

import com.lalit.order.dto.UpdateOrderStatusRequest;
import com.lalit.order.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import com.lalit.order.dto.OrderResponse;
import com.lalit.order.service.OrderService;
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

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest orderStatus
            ){
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId,orderStatus.getStatus()));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponse>> createOrderHistory(
            Authentication authentication
    ){
        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();
        return ResponseEntity.ok(orderService.orderHistory(userId));
    }
}
