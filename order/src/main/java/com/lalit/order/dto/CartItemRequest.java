package com.lalit.order.dto;

import lombok.Data;

@Data
public class CartItemRequest {

    private Long productId;
    private Integer quantity;
}
