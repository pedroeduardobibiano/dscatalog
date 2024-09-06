package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.dto.ProductMinDTO;
import com.devsuperior.dscatalog.entites.Category;
import com.devsuperior.dscatalog.entites.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductService(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Transactional(readOnly = true)
    public Page<ProductMinDTO> findAllPaged(Pageable pageable) {
        Page<Product> list = productRepository.findAll(pageable);
        return list.map(ProductMinDTO::new);
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = getById(id);
        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO insert(final ProductDTO productDTO) {
        Product entity = new Product();
        updateData(entity, productDTO);
        productRepository.save(entity);
        return new ProductDTO(entity);
    }


    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product entity = getById(id);
        updateData(entity, productDTO);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        Product entity = getById(id);
        productRepository.delete(entity);
    }

    private Product getById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new ResourceNotFoundException("id not found: " + id));
    }

    private void updateData(Product product, ProductDTO obj) {
        BeanUtils.copyProperties(obj, product, "id");

        product.getCategories().clear();


        List<Category> categories = categoryService.getAllCategories(obj.getCategories());
        product.getCategories().addAll(categories);
    }

}




