package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.CategoryRecord;
import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entites.Category;
import com.devsuperior.dscatalog.entites.Product;
import com.devsuperior.dscatalog.factory.CategoryFactory;
import com.devsuperior.dscatalog.factory.ProductFactory;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityExistsException;
import org.hibernate.sql.Update;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductFactory productFactory;

    public ProductService(ProductRepository productRepository, ProductFactory productFactory) {
        this.productRepository = productRepository;
        this.productFactory = productFactory;
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest) {
        Page<Product> list = productRepository.findAll(pageRequest);
        return list.map(ProductDTO::new);

    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Product entity = getById(id);
        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(final ProductDTO productDTO) {
        Product product = productFactory.createProduct(productDTO);
        productRepository.save(product);
        return new ProductDTO(product);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO productDTO) {
        Product entity = getById(id);
        updateData(entity, productDTO);
        entity = productRepository.save(entity);
        return new ProductDTO(entity);
    }

    private Product getById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElseThrow(() -> new ResourceNotFoundException("id not found: " + id));
    }

    private void updateData(Product product, ProductDTO obj) {
        product.setName(obj.getName());
        product.setDescription(obj.getDescription());
        product.setPrice(obj.getPrice());
        product.setImgUrl(obj.getImgUrl());
        product.setDate(obj.getDate());
    }

}




