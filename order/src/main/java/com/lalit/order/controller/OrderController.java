package com.lalit.order.controller;

import com.lalit.order.dto.UpdateOrderStatusRequest;
import com.lalit.order.enums.OrderStatus;
import com.lalit.order.security.JwtService;
import com.lalit.order.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
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
    private final JwtService  jwtService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            Authentication authentication
    ) {
        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();

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

    @PostMapping("/orderHistory")
    public ResponseEntity<List<OrderResponse>> createOrderHistory(
            Authentication authentication
    ){
        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        Long userId = principal.userId();
        return ResponseEntity.ok(orderService.orderHistory(userId));
    }
}
