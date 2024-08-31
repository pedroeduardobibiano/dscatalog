package com.devsuperior.dscatalog.resource;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.CategoryRecord;
import com.devsuperior.dscatalog.services.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<Page<CategoryDTO>> findAll(
            @PageableDefault(page = 0, size = 3, sort = "name", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<CategoryDTO> list = categoryService.findAllPaged(pageable);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
        CategoryDTO dto = categoryService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CategoryRecord> insert(@RequestBody CategoryDTO dto) {
        CategoryRecord insert = categoryService.insert(dto);
        return new ResponseEntity<>(insert, HttpStatus.CREATED);
    }


    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoryRecord> update(@PathVariable Long id, @RequestBody CategoryDTO dto) {
        dto.setId(id);
        CategoryRecord update = categoryService.update(dto);
        return new ResponseEntity<>(update, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
