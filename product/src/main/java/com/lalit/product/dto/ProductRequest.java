package com.lalit.product.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;
    @NotBlank(message = "Description is required")
    private String description;
    @NotNull(message = "Price is Required")
    @PositiveOrZero(message = "Price must be greater then '0'")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity must be greater then '0'")
    private Integer quantity;
    @NotBlank(message = "Category is required")
    private String category;
    @NotBlank(message = "Image URL is required")
    private String imageUrl;
}
