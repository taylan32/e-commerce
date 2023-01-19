package com.example.ecommerce.service;

import com.example.ecommerce.dto.converter.ProductDtoConverter;
import com.example.ecommerce.dto.product.CreateProductRequest;
import com.example.ecommerce.dto.product.ProductDto;
import com.example.ecommerce.dto.product.UpdateProductRequest;
import com.example.ecommerce.exception.BaseException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import com.example.ecommerce.service.filter.ProductFilter;
import com.example.ecommerce.service.filter.ProductSpec;
import com.example.ecommerce.utils.BasePageableModel;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

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

    public BasePageableModel<ProductDto> getAllProduct(ProductFilter productFilter, int pageNumber, int pageSize) {

        Specification<Product> specification = ProductSpec.filterBy(productFilter);

        Page<Product> data = productRepository.findAll(specification ,PageRequest.of(pageNumber - 1, pageSize));

        return new BasePageableModel<ProductDto>(
                data,
                data.getContent()
                        .stream()
                        .map(converter::convert)
                        .collect(Collectors.toList())
        );
    }

    @Transactional(rollbackOn = Exception.class)
    public ProductDto updateProduct(UpdateProductRequest request) {

        final Product product = findProductById(request.getId());
        updateAttributes(request, product);

        return converter.convert(productRepository.save(product));

    }

    public void deleteProduct(Long id) {
        Product product = findProductById(id);
        productRepository.delete(product);
    }

    public void updateAttributes(UpdateProductRequest request, Product product) {
        product.setProductName(getOrDefault(request.getProductName(), product.getProductName()));
        product.setPrice(getOrDefault(request.getPrice(), product.getPrice()));
        product.setUnitsInStock(getOrDefault(request.getUnitsInStock(), product.getUnitsInStock()));
        product.setDefinition(getOrDefault(request.getDefinition(), product.getDefinition()));
    }

    private <T> T getOrDefault(T data, T defaultValue) {
        return data == null ? defaultValue : data;
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
