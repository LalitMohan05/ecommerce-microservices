package com.lalit.product.controller;

import com.lalit.product.dto.ProductRequest;
import com.lalit.product.dto.ProductResponse;
import com.lalit.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Operation(
            summary = "Create Product",
            description = "Creates a new product in the catalog"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody ProductRequest productRequest
    ) {
        return new ResponseEntity<>(
                productService.createProduct(productRequest),
                HttpStatus.CREATED
        );
    }

    @Operation(
            summary = "Update Product",
            description = "Updates an existing active product"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "400", description = "Invalid product data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest
    ) {
        return ResponseEntity.ok(
                productService.updateProduct(id, productRequest)
        );
    }

    @Operation(
            summary = "Get All Products",
            description = "Returns all active products"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Products fetched successfully"
    )
    @GetMapping("/all")
    public ResponseEntity<List<ProductResponse>> getAllProduct() {
        return ResponseEntity.ok(
                productService.fetchAllProduct()
        );
    }

    @Operation(
            summary = "Get Product By ID",
            description = "Fetches a product by its id"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                productService.fetchProductById(id)
        );
    }

    @Operation(
            summary = "Delete Product",
            description = "Soft deletes a product by setting active to false"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(
            @PathVariable Long id
    ) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search Products",
            description = "Search products using a keyword"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Products fetched successfully"
    )
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchProducts(
            @RequestParam String keyword
    ) {
        return ResponseEntity.ok(
                productService.searchProducts(keyword)
        );
    }

    @Operation(
            summary = "Reduce Product Stock",
            description = "Reduces product inventory after order placement"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Stock reduced successfully"),
            @ApiResponse(responseCode = "400", description = "Insufficient stock"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PutMapping("/{id}/reduce-stock")
    public ResponseEntity<String> reduceStock(
            @PathVariable Long id,
            @RequestParam Integer quantity
    ) {
        productService.reduceStock(id, quantity);

        return ResponseEntity.ok(
                "Stock reduced successfully"
        );
    }
}