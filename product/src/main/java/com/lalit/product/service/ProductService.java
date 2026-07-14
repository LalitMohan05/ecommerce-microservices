package com.lalit.product.service;

import com.lalit.product.exceptions.InsufficientStockException;
import com.lalit.product.exceptions.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import com.lalit.product.dto.ProductRequest;
import com.lalit.product.dto.ProductResponse;
import com.lalit.product.entity.Product;
import com.lalit.product.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public ProductResponse updateProduct(Long id, ProductRequest productRequest) {
        Product existingProduct = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id : "+id));
        updateProductFromRequest(existingProduct, productRequest);
        Product savedProduct = productRepo.save(existingProduct);
        return mapToProductResponse(savedProduct);
    }

    public List<ProductResponse> fetchAllProduct() {
        return productRepo.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public ProductResponse fetchProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id : "+id));
        return mapToProductResponse(product);

    }

    public boolean deleteProduct(Long id) {
       return productRepo.findById(id)
               .map(product -> {
                   product.setActive(false);
                   productRepo.save(product);
                   return true;
               }).orElseThrow(() -> new ProductNotFoundException("Product not found with id : "+id));

    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepo.searchProducts(keyword).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    public void reduceStock(Long id, Integer quantity) {

        Product product= productRepo.findById(id)
                .orElseThrow(()->
                        new ProductNotFoundException("Product not found with id : "+id));

        if(product.getQuantity()<quantity){
            throw new InsufficientStockException(
                    "Only "+product.getQuantity() +" units available for product: "+
                            product.getName()
            );
        }

        product.setQuantity(
                product.getQuantity()-quantity
        );

        productRepo.save(product);
    }
}
