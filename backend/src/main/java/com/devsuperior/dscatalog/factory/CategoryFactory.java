package com.devsuperior.dscatalog.factory;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.CategoryRecord;
import com.devsuperior.dscatalog.entites.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryFactory {

    public Category createCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.getName());
        return category;
    }

    public CategoryRecord create(Long id, String name) {
        return new CategoryRecord(id, name);
    }
}


