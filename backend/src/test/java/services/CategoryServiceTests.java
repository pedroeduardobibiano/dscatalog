package services;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.CategoryRecord;
import com.devsuperior.dscatalog.entites.Category;
import com.devsuperior.dscatalog.factory.CategoryFactory;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.services.CategoryService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class CategoryServiceTests {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryFactory categoryFactory;

    private CategoryDTO categoryDTO;
    private Category newCategory;
    private Category saveCategory;

    private Long existId;
    private Long noExistId;
    private Long dependentId;

    @BeforeEach
    void setUp() {
        existId = 1L;
        noExistId = 100L;
        dependentId = 4L;

        categoryDTO = new CategoryDTO();
        categoryDTO.setName("Jardinagem");
        categoryDTO.setId(1L);

        newCategory = new Category();
        newCategory.setName(categoryDTO.getName());
        newCategory.setId(categoryDTO.getId());

        saveCategory = new Category();
        saveCategory.setId(1L);
        saveCategory.setName(categoryDTO.getName());
    }

    @Test
    public void findAllPagedShouldReturnPage() {
        PageImpl<Category> page = new PageImpl<>(List.of(newCategory));

        Mockito.when(categoryRepository.findAll((Pageable) ArgumentMatchers.any())).thenReturn(page);

        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryDTO> result = categoryService.findAllPaged(pageable);
        Assertions.assertNotNull(result);

        Mockito.verify(categoryRepository, Mockito.times(1)).findAll(pageable);

    }

    @Test
    public void findByIdShouldReturnCategory() {

        Mockito.when(categoryRepository.findById(existId)).thenReturn(Optional.of(saveCategory));

        Assertions.assertDoesNotThrow(() -> categoryService.findById(existId));

        Mockito.verify(categoryRepository, Mockito.times(1)).findById(existId);

    }

    @Test
    public void findByIdShouldThrowResourceNotFoundWhenIdIsNotFound() {

        Mockito.when(categoryRepository.findById(noExistId)).thenReturn(Optional.empty());

        Assertions.assertThrows((ResourceNotFoundException.class), () -> {
            categoryService.findById(noExistId);
        });

        Mockito.verify(categoryRepository, Mockito.times(1)).findById(noExistId);

    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdNotExist() {

        Mockito.when(categoryRepository.existsById(noExistId)).thenReturn(false);

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.delete(noExistId);
        });

        Mockito.verify(categoryRepository, Mockito.times(1)).existsById(noExistId);

    }

    @Test
    public void deleteShouldThrowDataIntegrityViolationExceptionWhenDependentId() {

        Mockito.doThrow(DataIntegrityViolationException.class).when(categoryRepository).existsById(dependentId);

        Assertions.assertThrows(DatabaseException.class, () -> {
            categoryService.delete(dependentId);
        });

        Mockito.verify(categoryRepository, Mockito.times(1)).existsById(dependentId);

    }

    @Test
    public void deleteShouldDeleteCategoryObjectWhenIdExist() {

        Mockito.when(categoryRepository.findById(existId)).thenReturn(Optional.of(newCategory));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.delete(existId);
        });
    }


    @Test
    public void insertShouldThrowEntityExistsExceptionWhenCategoryNameExists() {
        String nameCat = "Alimentação";
        categoryDTO.setName(nameCat);


        Mockito.when(categoryRepository.findByName(nameCat))
                .thenReturn(Optional.of(new Category()));

        Assertions.assertThrows(EntityExistsException.class, () -> categoryService.insert(categoryDTO));

        Mockito.verify(categoryRepository, Mockito
                .times(1)).findByName(nameCat);

    }

    @Test
    public void insertShouldInsertCategoryObjectWhenCategoryNameDoesNotExistsInData() {

        Mockito.when(categoryRepository.findByName(categoryDTO.getName()))
                .thenReturn(Optional.empty());

        Mockito.when(categoryFactory.createCategory(categoryDTO))
                .thenReturn(newCategory);

        Mockito.when(categoryRepository.save(newCategory))
                .thenReturn(saveCategory);


        Assertions.assertDoesNotThrow(() -> {
            CategoryRecord serviceInsert = categoryService.insert(categoryDTO);

            Assertions.assertEquals(categoryDTO.getName(), serviceInsert.name());
            Assertions.assertEquals(saveCategory.getId(), serviceInsert.id());


            Mockito.verify(categoryRepository, Mockito.times(1))
                    .save(newCategory);

            Mockito.verify(categoryRepository, Mockito.times(1))
                    .findByName(categoryDTO.getName());

            Mockito.verify(categoryFactory, Mockito.times(1))
                    .createCategory(categoryDTO);

        });

    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdNoExist() {
        categoryDTO.setId(noExistId);

        Mockito.when(categoryRepository.findById(noExistId)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.update(categoryDTO);
        });

        Mockito.verify(categoryRepository, Mockito.times(1)).findById(noExistId);

    }

}
