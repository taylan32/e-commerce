package com.example.ecommerce.service;

import com.example.ecommerce.dto.converter.ProductDtoConverter;
import com.example.ecommerce.dto.product.CreateProductRequest;
import com.example.ecommerce.dto.product.ProductDto;
import com.example.ecommerce.exception.BaseException;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ProductServiceTest {

    private ProductService productService;
    private ProductRepository productRepository;
    private CategoryService categoryService;
    private ProductDtoConverter converter;

    @BeforeEach
    public void setUp() {

        productRepository = mock(ProductRepository.class);
        categoryService = mock(CategoryService.class);
        converter = mock(ProductDtoConverter.class);
        productService = new ProductService(productRepository, categoryService, converter);

    }

    @Test
    public void testFindProductById_whenProductIdExists_shouldReturnProduct() {
        Product product = generateProduct();
        Mockito.when(productRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(product));

        Product result = productService.findProductById(Long.valueOf(1));

        assertEquals(product, result);

    }


    @Test
    public void testFindProductById_whenProductIdDoesNotExist_shouldThrowException() {
        Mockito.when(productRepository.findById(Long.valueOf(1))).thenReturn(Optional.empty());

        assertThrows(BaseException.class, () -> productService.findProductById(Long.valueOf(1)));
    }

    @Test
    public void testGetProductById_whenProductIdExists_shouldReturnProductDto() {
        Product product = generateProduct();
        ProductDto productDto = new ProductDto(
                product.getId(),
                product.getProductName(),
                product.getPrice(),
                product.getUnitsInStock(),
                product.getDefinition(),
                product.getCategory()
        );
        Mockito.when(productRepository.findById(Long.valueOf(1))).thenReturn(Optional.of(product));
        Mockito.when(converter.convert(product)).thenReturn(productDto);

        ProductDto result = productService.getProductById(product.getId());

        assertEquals(productDto, result);

    }

    @Test
    public void testGetProductById_whenProductIdDoesNotExist_shouldThrowException() {
        Mockito.when(productRepository.findById(Long.valueOf(1))).thenReturn(Optional.empty());
        assertThrows(BaseException.class, () -> productService.getProductById(Long.valueOf(1)));

        Mockito.verifyNoInteractions(converter);

    }

    @Test
    public void testCreateProduct_whenCategoryIdExists_shouldReturnProductDto() {
        Category category = generateCategory();
        CreateProductRequest request = new CreateProductRequest(
                "product-name",
                new BigDecimal(100),
                10,
                "product-definition",
                Long.valueOf(1)
        );

        Product product = generateProduct();

        ProductDto expected = new ProductDto(
                Long.valueOf(1),
                "productName",
                new BigDecimal(100),
                10,
                "product-definition",
                category
        );

        Mockito.when(productRepository.save(product)).thenReturn(product);
        Mockito.when(converter.convert(product)).thenReturn(expected);
        Mockito.when(productService.createProduct(request)).thenReturn(expected);

        ProductDto result = productService.createProduct(request);

        assertEquals(expected, result);

    }

    public Product generateProduct() {
        Category category = generateCategory();
        return new Product(
                Long.valueOf(1),
                "product-name",
                new BigDecimal(100),
                10, "product-definition",
                category);
    }

    public Category generateCategory() {
        return new Category(Long.valueOf(1), "category-name");
    }


}
