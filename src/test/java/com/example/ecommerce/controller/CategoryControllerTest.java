package com.example.ecommerce.controller;

import com.example.ecommerce.dto.converter.CategoryDtoConverter;
import com.example.ecommerce.dto.category.CategoryDto;
import com.example.ecommerce.dto.category.CreateCategoryRequest;
import com.example.ecommerce.model.Category;
import com.example.ecommerce.repository.CategoryRepository;
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
class CategoryControllerTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private CategoryDtoConverter converter;
    private final String CATEGORY_API_ENDPOINT = "/api/v1/category";

    @BeforeEach
    public void setUp() {}

    @Test
    public void testCreateCategory_ShouldCreateCategoryAndReturnCategoryDto() throws Exception{
        CreateCategoryRequest request = new CreateCategoryRequest("name");
        mockMvc.perform(post(CATEGORY_API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.categoryName", is(request.getCategoryName())));

    }

    @Test
    public void testCreateCategory_whenCategoryNameAlreadyExists_shouldReturnHttpBadRequest() throws Exception {
        Category category = categoryRepository.save(new Category(null, "category-name"));
        CreateCategoryRequest request = new CreateCategoryRequest("category-name");
        mockMvc.perform(post(CATEGORY_API_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


    }

    @Test
    public void testGetById_whenCategoryIdExists_shouldReturnCategoryDtoAndHttpOK() throws Exception {
        Category category = categoryRepository.save(new Category(Long.valueOf(1), "category-name"));
        CategoryDto exptected = converter.convert(
                categoryRepository.getById(Objects.requireNonNull(category.getId()))
        );

        mockMvc.perform(get(CATEGORY_API_ENDPOINT + "/" + category.getId()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(mapper.writer().withDefaultPrettyPrinter().writeValueAsString(exptected), false))
                .andReturn();

    }

    @Test
    public void testGetById_whenCategoryIdDoesNotExist_shouldReturnHttpNotFound() throws Exception {

        mockMvc.perform(get(CATEGORY_API_ENDPOINT + "/" + 0))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

    }

    @Test
    public void testGetAllCategory_shouldReturnHttpOK() throws Exception {
        mockMvc.perform(get(CATEGORY_API_ENDPOINT))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }


}
