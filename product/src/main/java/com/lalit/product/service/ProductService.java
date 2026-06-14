package com.lalit.product.service;

import lombok.RequiredArgsConstructor;
import com.lalit.product.dto.ProductRequest;
import com.lalit.product.dto.ProductResponse;
import com.lalit.product.entity.Product;
import com.lalit.product.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepo productRepo;
    public ProductResponse createProduct(ProductRequest productRequest) {
        Product product= new Product();
        updateProductFromRequest(product,productRequest);
        Product savedProduct = productRepo.save(product);
        return mapToProductResponse(savedProduct);
    }

    private ProductResponse mapToProductResponse(Product savedProduct) {
        ProductResponse response= new ProductResponse();
        response.setId(savedProduct.getId());
        response.setName(savedProduct.getName());
        response.setActive(savedProduct.getActive());
        response.setCatagory(savedProduct.getCategory());
        response.setDescription(savedProduct.getDescription());
        response.setPrice(savedProduct.getPrice());
        response.setImageUrl(savedProduct.getImageUrl());
        response.setQuantity(savedProduct.getQuantity());

        return response;


    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setImageUrl(productRequest.getImageUrl());
        product.setQuantity(productRequest.getQuantity());
    }

    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepo.findById(id)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct, productRequest);
                    Product savedProduct = productRepo.save(existingProduct);
                    return mapToProductResponse(savedProduct);
                });
    }

    public List<ProductResponse> fetchAllProduct() {
        return productRepo.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProductResponse> fetchProductById(Long id) {
        return productRepo.findById(id)
                .map(this::mapToProductResponse);

    }

    public boolean deleteProduct(Long id) {
       return productRepo.findById(id)
               .map(product -> {
                   product.setActive(false);
                   productRepo.save(product);
                   return true;
               }).orElse(false);

    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepo.searchProducts(keyword).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public void reduceStock(Long id, Integer quantity) {

        Product product= productRepo.findById(id)
                .orElseThrow(()->
                        new RuntimeException("Product not found"));

        if(product.getQuantity()<quantity){
            throw new RuntimeException(
                    "Insufficient stock for product" +
                            product.getName()
            );
        }

        product.setQuantity(
                product.getQuantity()-quantity
        );

        productRepo.save(product);
    }
}
