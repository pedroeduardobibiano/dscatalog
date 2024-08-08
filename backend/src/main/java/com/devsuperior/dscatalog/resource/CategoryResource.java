package com.devsuperior.dscatalog.resource;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.dto.CategoryRecord;
import com.devsuperior.dscatalog.services.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CategoryDTO>> findAll() {
        List<CategoryDTO> list = categoryService.findAll();
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
        CategoryRecord insert = categoryService.update(id, dto);
        return new ResponseEntity<>(insert, HttpStatus.OK);
    }

}
