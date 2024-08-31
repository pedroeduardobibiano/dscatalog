package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.RoleDTO;
import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.entites.Role;
import com.devsuperior.dscatalog.entites.User;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository UserRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(com.devsuperior.dscatalog.repositories.UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        UserRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> list = UserRepository.findAll(pageable);
        return list.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User entity = getById(id);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(final UserInsertDTO userDTO) {
        User entity = new User();
        updateData(entity, userDTO);
        entity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserRepository.save(entity);
        return new UserDTO(entity);
    }


    @Transactional
    public UserDTO update(Long id, UserDTO UserDTO) {
        User entity = getById(id);
        updateData(entity, UserDTO);
        entity = UserRepository.save(entity);
        return new UserDTO(entity);
    }

    @Transactional
    public void delete(Long id) {
        User entity = getById(id);
        UserRepository.delete(entity);
    }


    private User getById(Long id) {
        Optional<User> user = UserRepository.findById(id);
        return user.orElseThrow(() -> new ResourceNotFoundException("id not found: " + id));
    }

    private void updateData(User user, UserDTO obj) {
        BeanUtils.copyProperties(obj, user, "id");

        obj.getRoles().clear();
        for (RoleDTO roleDTO : obj.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            user.getRoles().add(role);
        }


    }

}




