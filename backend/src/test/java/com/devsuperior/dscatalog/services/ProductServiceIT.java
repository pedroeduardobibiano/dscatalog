//package com.devsuperior.dscatalog.services;
//
//import com.devsuperior.dscatalog.dto.ProductDTO;
//import com.devsuperior.dscatalog.dto.ProductMinDTO;
//import com.devsuperior.dscatalog.repositories.ProductRepository;
//import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Sort;
//import org.springframework.transaction.annotation.Transactional;
//
//@SpringBootTest
//@Transactional
//public class ProductServiceIT {
//
//    @Autowired
//    private ProductService productService;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    private Long existId;
//    private Long noExistId;
//    private Long countTotalProducts;
//
//
//    @BeforeEach
//    public void setUp() {
//        existId = 1L;
//        noExistId = 100L;
//        countTotalProducts = 25L;
//
//    }
//
//    @Test
//    public void deleteShouldDeleteResourceWhenIdExists() {
//        productService.delete(existId);
//
//        Assertions.assertEquals(productRepository.count(), countTotalProducts - 1);
//    }
//
//    @Test
//    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
//
//        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
//            productService.delete(noExistId);
//        });
//
//    }
//
//    @Test
//    public void findAllPagedShouldReturnAllProductsWhenPage0Size10() {
//        PageRequest pageRequest = PageRequest.of(0, 10);
//
//        Page<ProductDTO> result = productService.findAllPaged(0L, "", pageRequest);
//
//        Assertions.assertFalse(result.isEmpty());
//        Assertions.assertEquals(0, result.getNumber());
//        Assertions.assertEquals(10, result.getSize());
//        Assertions.assertEquals(countTotalProducts, result.getTotalElements());
//
//    }
//
//    @Test
//    public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
//        PageRequest pageRequest = PageRequest.of(50, 10);
//
//        Page<ProductDTO> result = productService.findAllPaged(0L, "", pageRequest);
//
//        Assertions.assertTrue(result.isEmpty());
//
//    }
//
//    @Test
//    public void findAllPagedShouldReturnOrderedPageWhenSortByName() {
//        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
//
//        Page<ProductDTO> result = productService.findAllPaged(0L, "", pageRequest);
//
//        Assertions.assertFalse(result.isEmpty());
//        Assertions.assertEquals("Macbook Pro", result.getContent().get(0).getName());
//        Assertions.assertEquals("PC Gamer", result.getContent().get(1).getName());
//        Assertions.assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());
//
//    }
//}
