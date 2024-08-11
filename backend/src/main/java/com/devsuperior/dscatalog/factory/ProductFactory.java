package com.devsuperior.dscatalog.factory;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entites.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductFactory {

    public Product createProduct(ProductDTO productDTO) {
        Product entity = new Product();
        entity.setName(productDTO.getName());
        entity.setDescription(productDTO.getDescription());
        entity.setPrice(productDTO.getPrice());
        entity.setImgUrl(productDTO.getImgUrl());
        entity.setDate(productDTO.getDate());
        return entity;
    }


}
