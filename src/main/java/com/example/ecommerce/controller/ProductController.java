package com.example.ecommerce.controller;

import com.example.ecommerce.dto.product.CreateProductRequest;
import com.example.ecommerce.dto.product.ProductDto;
import com.example.ecommerce.dto.product.UpdateProductRequest;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.ProductService;
import com.example.ecommerce.service.filter.ProductFilter;
import com.example.ecommerce.utils.BasePageableModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    private final ProductService productService;
    private final ProductRepository productRepository;

    public ProductController(ProductService productService,
                             ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@RequestBody @Valid CreateProductRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<BasePageableModel<ProductDto>> getAll(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "priceFrom" ,required = false) BigDecimal priceFrom,
            @RequestParam(name = "priceTo",required = false) BigDecimal priceTo,
            @RequestParam(name = "stockFrom", required = false) Integer stockFrom,
            @RequestParam(name = "stockTo", required = false) Integer stockTo,
            @RequestParam(name = "category", required = false) Long category,
            @RequestParam(defaultValue = "1", required = false) int pageNumber,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ) {

        return ResponseEntity.ok(productService.getAllProduct(
                new ProductFilter(
                        name,
                        priceFrom,
                        priceTo,
                        stockFrom,
                        stockTo,
                        category
                ),
                pageNumber,
                pageSize)
        );
    }


    @PutMapping("/update")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody @Valid UpdateProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(request));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
