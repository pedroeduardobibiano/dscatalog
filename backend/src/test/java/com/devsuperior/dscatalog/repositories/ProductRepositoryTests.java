package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entites.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;
import tests.Factory;

import java.util.Optional;

@DataJpaTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class ProductRepositoryTests {

    private final ProductRepository productRepository;

    public ProductRepositoryTests(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    private long existingId;
    private long nonExistingId;
    private long countTotalProducts;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists() {

        productRepository.deleteById(existingId);

        Optional<Product> result = productRepository.findById(existingId);
        Assertions.assertFalse(result.isPresent());
    }

    @Test
    public void saveShouldPersistWithAutoIncrementWhenIdIsNull() {
        Product product = Factory.createProduct();
        product.setId(null);

        product = productRepository.save(product);

        Assertions.assertNotNull(product.getId());
        Assertions.assertEquals(countTotalProducts + 1, product.getId());
    }

    @Test
    public void finByIdShouldReturnProductObjectWhenIdExist() {
        Optional<Product> result = productRepository.findById(existingId);

        Assertions.assertTrue(result.isPresent());

    }

    @Test
    public void finByIdShouldReturnNullObjectWhenIdNotExist() {
        Optional<Product> result = productRepository.findById(nonExistingId);

        Assertions.assertTrue(result.isEmpty());

    }

}
