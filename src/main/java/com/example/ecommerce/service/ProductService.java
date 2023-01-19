package com.example.ecommerce.service;

import com.example.ecommerce.converter.ProductDtoConverter;
import com.example.ecommerce.dto.product.CreateProductRequest;
import com.example.ecommerce.dto.product.ProductDto;
import com.example.ecommerce.exception.BaseException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductDtoConverter converter;

    public ProductService(ProductRepository productRepository, CategoryService categoryService, ProductDtoConverter converter) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.converter = converter;
    }

    public ProductDto createProduct(CreateProductRequest request) {

        Category category = categoryService.findCategoryById(request.getCategoryId());
        checkIfProductNameExists(request.getProductName());

        return converter.convert(productRepository.save(
                new Product(
                null,
                request.getProductName(),
                request.getPrice(),
                request.getUnitsInStock(),
                request.getDefinition(),
                category
        )));
    }


    public ProductDto getProductById(Long id) {
        return converter.convert(findProductById(id));
    }

    protected Product findProductById(Long id) {
        return productRepository.findById(id).orElseThrow(() -> BaseException
                .builder()
                .errorMessage("Requested product not found")
                .httpStatus(HttpStatus.NOT_FOUND)
                .build());
    }

    public List<ProductDto> getAllProduct() {
        return productRepository.findAll()
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    private void checkIfProductNameExists(String productName) {
        if(productRepository.existsByProductName(productName.strip())) {
            throw BaseException.builder()
                    .errorMessage("Product name already exists")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }

}
