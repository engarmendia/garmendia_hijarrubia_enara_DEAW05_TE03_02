package com.example.demo.repository;

import com.example.demo.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .name("Test Product")
                .price(9.99)
                .description("Test Description")
                .build();
    }

    @Test
    void shouldSaveProduct() {
        // given
        // product ya inicializado en setUp()

        // when
        Product savedProduct = productRepository.save(product);

        // then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isPositive();
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
    }

    @Test
    void shouldFindProductById() {
        // given
        Product savedProduct = entityManager.persistAndFlush(product);

        // when
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        // then
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo(savedProduct.getName());
    }

    @Test
    void shouldFindAllProducts() {
        // given
        entityManager.persistAndFlush(product);
        entityManager.persistAndFlush(Product.builder()
                .name("Another Product")
                .price(19.99)
                .build());

        // when
        List<Product> products = productRepository.findAll();

        // then
        assertThat(products).hasSize(2);
    }

    @Test
    void shouldUpdateProduct() {
        // given
        Product savedProduct = entityManager.persistAndFlush(product);
        savedProduct.setName("Updated Name");

        // when
        Product updatedProduct = productRepository.save(savedProduct);

        // then
        assertThat(updatedProduct.getName()).isEqualTo("Updated Name");
    }

    @Test
    void shouldDeleteProduct() {
        // given
        Product savedProduct = entityManager.persistAndFlush(product);

        // when
        productRepository.delete(savedProduct);

        // then
        assertThat(productRepository.findById(savedProduct.getId())).isEmpty();
    }
}