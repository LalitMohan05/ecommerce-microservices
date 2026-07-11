package com.lalit.order.controller;

import com.lalit.order.dto.UpdateOrderStatusRequest;
import com.lalit.order.enums.OrderStatus;
import lombok.RequiredArgsConstructor;
import com.lalit.order.dto.OrderResponse;
import com.lalit.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("X-User-ID") Long userId) {

        return orderService.createOrder(userId)
                .map(orderResponse -> new ResponseEntity<>(orderResponse,HttpStatus.CREATED))
                .orElseGet(()->ResponseEntity.badRequest().build());
    }

    @PutMapping("/{OrderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long OrderId,
            @RequestBody UpdateOrderStatusRequest orderStatus
            ){
        return ResponseEntity.ok(orderService.updateOrderStatus(OrderId,orderStatus.getStatus()));
    }
}
