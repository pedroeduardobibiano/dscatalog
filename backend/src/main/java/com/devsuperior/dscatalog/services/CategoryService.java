package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.CategoryRecord;
import com.devsuperior.dscatalog.entites.Category;
import com.devsuperior.dscatalog.factory.CategoryFactory;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityExistsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryFactory categoryFactory;

    public CategoryService(CategoryRepository categoryRepository, CategoryFactory categoryFactory) {
        this.categoryRepository = categoryRepository;
        this.categoryFactory = categoryFactory;
    }

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        List<Category> list = categoryRepository.findAll();
        return list.stream().map(CategoryDTO::new).toList();

    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category entity = getById(id);
        return new CategoryDTO(entity);
    }

    @Transactional
    public CategoryRecord insert(final CategoryDTO categoryDTO) {
        categoryRepository.findByName(categoryDTO.getName())
                .ifPresent(x -> {
                    throw new EntityExistsException("Category already exists");
                });
        Category entity = categoryFactory.createCategory(categoryDTO);
        entity = categoryRepository.save(entity);
        return new CategoryRecord(entity.getId(), entity.getName());

    }

    @Transactional
    public CategoryRecord update(Long id, CategoryDTO categoryDTO) {
        Category category = getById(id);
        category.setName(categoryDTO.getName());

        category = categoryRepository.save(category);
        return categoryFactory.create(category.getId(), category.getName());
    }

    private Category getById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.orElseThrow(() -> new ResourceNotFoundException("id not found: " + id));
    }

}




