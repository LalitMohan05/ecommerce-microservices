package com.lalit.order.dto;

import com.lalit.order.enums.OrderStatus;
import lombok.Data;

@Data
public class UpdateOrderStatusRequest {
    private OrderStatus status;
}
