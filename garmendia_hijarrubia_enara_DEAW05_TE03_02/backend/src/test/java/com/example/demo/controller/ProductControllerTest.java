package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        productRepository.deleteAll();
    }

    @Test
    void shouldCreateNewProduct() throws Exception {
        // given
        Product product = Product.builder()
                .name("Laptop")
                .price(999.99)
                .description("High performance laptop")
                .build();

        // when
        ResultActions response = mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)));

        // then
        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.price", is(product.getPrice())))
                .andExpect(jsonPath("$.description", is(product.getDescription())));
    }

    @Test
    void shouldReturnListOfProducts() throws Exception {
        // given
        List<Product> products = new ArrayList<>();
        products.add(Product.builder().name("Phone").price(599.99).build());
        products.add(Product.builder().name("Tablet").price(299.99).build());
        productRepository.saveAll(products);

        // when
        ResultActions response = mockMvc.perform(get("/api/products"));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(products.size())));
    }

    @Test
    void shouldReturnProductById() throws Exception {
        // given
        Product product = Product.builder()
                .name("Monitor")
                .price(199.99)
                .build();
        productRepository.save(product);

        // when
        ResultActions response = mockMvc.perform(get("/api/products/{id}", product.getId()));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(product.getName())))
                .andExpect(jsonPath("$.price", is(product.getPrice())));
    }

    @Test
    void shouldUpdateProduct() throws Exception {
        // given
        Product savedProduct = Product.builder()
                .name("Keyboard")
                .price(49.99)
                .build();
        productRepository.save(savedProduct);

        Product updatedProduct = Product.builder()
                .name("Mechanical Keyboard")
                .price(89.99)
                .description("RGB backlit")
                .build();

        // when
        ResultActions response = mockMvc.perform(put("/api/products/{id}", savedProduct.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedProduct)));

        // then
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.name", is(updatedProduct.getName())))
                .andExpect(jsonPath("$.price", is(updatedProduct.getPrice())))
                .andExpect(jsonPath("$.description", is(updatedProduct.getDescription())));
    }

    @Test
    void shouldDeleteProduct() throws Exception {
        // given
        Product product = Product.builder()
                .name("Mouse")
                .price(25.99)
                .build();
        productRepository.save(product);

        // when
        ResultActions response = mockMvc.perform(delete("/api/products/{id}", product.getId()));

        // then
        response.andExpect(status().isNoContent())
                .andDo(print());
    }
}