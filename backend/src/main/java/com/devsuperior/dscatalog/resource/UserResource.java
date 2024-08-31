package com.devsuperior.dscatalog.resource;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.entites.User;
import com.devsuperior.dscatalog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    public final UserService userService;

    public UserResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAll(
            @PageableDefault(page = 0, size = 3, sort = "firstName", direction = Sort.Direction.DESC)
            Pageable pageable
    ) {
        Page<UserDTO> list = userService.findAllPaged(pageable);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
        UserDTO dto = userService.findById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) {
        UserDTO user = userService.insert(dto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @PutMapping(value = "/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id,@Valid @RequestBody UserDTO dto) {
        UserDTO userDto = userService.update(id, dto);
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

