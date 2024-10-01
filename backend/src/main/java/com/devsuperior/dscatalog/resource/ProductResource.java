package com.devsuperior.dscatalog.resource;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entites.Product;
import com.devsuperior.dscatalog.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/products")
public class ProductResource {

    public final ProductService productService;

    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDTO>> findAll(
            @RequestParam(value = "categoryId", required = false) Long categoryId,
            @RequestParam(value = "name", defaultValue = "") String name,
            @PageableDefault(page = 0, size = 3, sort = "name", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<ProductDTO> list = productService.findAllPaged(categoryId, name.trim(), pageable);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_OPERATOR')")
    @GetMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> findById(@PathVariable Long id) {
        ProductDTO dto = productService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto) {
        ProductDTO product = productService.insert(dto);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    private ProductDTO fromDTO(Product pd) {
        ProductDTO dto = new ProductDTO();
        dto.setId(pd.getId());
        dto.setName(pd.getName());
        dto.setDescription(pd.getDescription());
        dto.setPrice(pd.getPrice());
        return dto;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProductDTO> update(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        ProductDTO productDto = productService.update(id, dto);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

