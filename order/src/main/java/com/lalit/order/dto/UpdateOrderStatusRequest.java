package com.lalit.order.dto;

import com.lalit.order.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    @NotNull(message = "Order Status is required")
    private OrderStatus status;
}
