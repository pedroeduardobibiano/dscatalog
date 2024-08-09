package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.CategoryRecord;
import com.devsuperior.dscatalog.entites.Category;
import com.devsuperior.dscatalog.factory.CategoryFactory;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityExistsException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Page<CategoryDTO> findAllPaged(PageRequest pageRequest) {
        Page<Category> list = categoryRepository.findAll(pageRequest);
        return list.map(CategoryDTO::new);

    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category entityId = getById(id);
        return new CategoryDTO(entityId);
    }

    @Transactional
    public CategoryRecord insert(final CategoryDTO categoryDTO) {
        categoryRepository.findByName(categoryDTO.getName())
                .ifPresent(x -> {
                    throw new EntityExistsException("Category already exists");
                });
        Category cat = categoryFactory.createCategory(categoryDTO);
        cat = categoryRepository.save(cat);
        return new CategoryRecord(cat.getId(), cat.getName());
    }

    @Transactional
    public CategoryRecord update(CategoryDTO categoryDTO) {
        Category category = getById(categoryDTO.getId());
        category.setName(categoryDTO.getName());

        category = categoryRepository.save(category);
        return new CategoryRecord(category.getId(), category.getName());
    }


    public void delete(Long id) {
        try {
            if (categoryRepository.existsById(id)) {
                categoryRepository.deleteById(id);
            } else {
                throw new ResourceNotFoundException("id not found");
            }

        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private Category getById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.orElseThrow(() -> new ResourceNotFoundException("id not found: " + id));
    }

}




