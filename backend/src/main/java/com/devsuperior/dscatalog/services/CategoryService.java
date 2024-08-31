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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public Page<CategoryDTO> findAllPaged(Pageable pageable) {
        Page<Category> list = categoryRepository.findAll(pageable);
        return list.map(CategoryDTO::new);

    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category entityId = getById(id);
        return new CategoryDTO(entityId);
    }

    @Transactional
    public CategoryRecord insert(final CategoryDTO categoryDTO) {
        testIfExistCategoryNameEqual(categoryDTO);
        Category cat = categoryFactory.createCategory(categoryDTO);
        cat = categoryRepository.save(cat);
        return new CategoryRecord(cat.getId(), cat.getName());
    }


    @Transactional
    public CategoryRecord update(CategoryDTO categoryDTO) {
        testIfExistCategoryNameEqual(categoryDTO);
        Category category = getById(categoryDTO.getId());
        category.setName(categoryDTO.getName());

        category = categoryRepository.save(category);
        return new CategoryRecord(category.getId(), category.getName());
    }

    public void testIfExistCategoryNameEqual(CategoryDTO categoryDTO) {
        categoryRepository.findByName(categoryDTO.getName())
                .ifPresent(x -> {
                    throw new EntityExistsException("Category already exists");
                });
    }


    public void delete(Long id) {
        try {
            if (!categoryRepository.existsById(id)) {
                throw new ResourceNotFoundException("id not found");
            }
            categoryRepository.deleteById(id);

        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private Category getById(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.orElseThrow(() -> new ResourceNotFoundException("id not found: " + id));
    }

    public List<Category> getAllCategories(List<CategoryDTO> categoryDTOList) {
        List<Category> list = new ArrayList<>();
        for (CategoryDTO catDto : categoryDTOList) {
            Category category = categoryRepository.getReferenceById(catDto.getId());
            list.add(category);
        }
        return list;
    }

}




