package com.example.ecommerce.controller;

import com.example.ecommerce.converter.ProductDtoConverter;
import com.example.ecommerce.dto.product.CreateProductRequest;
import com.example.ecommerce.dto.product.ProductDto;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.repository.CategoryRepository;
import com.example.ecommerce.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
        "server-port=0",
        "command.line.runner.enabled=false"
})
@RunWith(SpringRunner.class)
@DirtiesContext
@Transactional
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDtoConverter converter;

    @Autowired
    private CategoryRepository categoryRepository;

    private final String PRODUCT_API_ENDPOINT = "/api/v1/product";

    @BeforeEach
    public void setUp() {}

    @Test
    public void testCreateProduct_whenCategoryIdExists_shouldCreateProductAndReturnProductDto() throws Exception {
        Category category = generateCategory();
        categoryRepository.save(category);
        CreateProductRequest request = new CreateProductRequest(
                "product-name",
                new BigDecimal(100),
                10,
                "product-definition",
                category.getId()
        );

        mockMvc.perform(post(PRODUCT_API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.productName", is(request.getProductName())))
                .andExpect(jsonPath("$.price", is(Integer.valueOf(Integer.valueOf(request.getPrice().toString())))))
                .andExpect(jsonPath("$.unitsInStock", is(request.getUnitsInStock())))
                .andExpect(jsonPath("$.definition", is(request.getDefinition())))
                .andExpect(jsonPath("$.category.id", is(Integer.valueOf(category.getId().toString()))))
                .andExpect(jsonPath("$.category.categoryName", is(category.getCategoryName())));

    }

    @Test
    public void testCreateProduct_whenCategoryDoesNotExist_shouldReturnHttpNotFound() throws Exception {
        Category category = generateCategory();
        categoryRepository.save(category);
        CreateProductRequest request = new CreateProductRequest(
                "product-name",
                new BigDecimal(100),
                10,
                "product-definition",
                Long.valueOf(2)
        );

        mockMvc.perform(post(PRODUCT_API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

    }

    @Test
    public void testCreateProduct_whenProductIsNotValid_shouldReturnHttpBadRequest() throws Exception {
        Category category = generateCategory();
        categoryRepository.save(category);
        CreateProductRequest request = new CreateProductRequest(
                null,null,-1,null, category.getId()
        );

        mockMvc.perform(post(PRODUCT_API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

    }

    @Test
    public void testGetProductById_whenProductIdExists_shouldReturnProductDto() throws Exception {
        Category category = generateCategory();
        Category saved = categoryRepository.save(category);
        Product product = productRepository.save(generateProduct(saved));
        ProductDto expected = converter.convert(
                productRepository.getById(
                        Objects.requireNonNull(product.getId())
                )
        );

        mockMvc.perform(get(PRODUCT_API_ENDPOINT + "/" + product.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(expected), false))
                .andReturn();

    }

    @Test
    public void testGetProductById_whenProductIdDoesNotExist_shouldReturnHttpNotFound() throws Exception {
        mockMvc.perform(get(PRODUCT_API_ENDPOINT + "/" + 1))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }


    public Category generateCategory() {
        return new Category(Long.valueOf(1), "category-name");
    }
    public Product generateProduct(Category category) {
        return new Product(
                Long.valueOf(1),
                "product-name",
                new BigDecimal(100),
                10,
                "product-definition",
                category);
    }

}