package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.RoleDTO;
import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.dto.UserInsertDTO;
import com.devsuperior.dscatalog.dto.UserLoggedDTO;
import com.devsuperior.dscatalog.dto.exception.UserDetailsDTO;
import com.devsuperior.dscatalog.entites.Role;
import com.devsuperior.dscatalog.entites.User;
import com.devsuperior.dscatalog.projections.UserDetailsProjection;
import com.devsuperior.dscatalog.repositories.RoleRepository;
import com.devsuperior.dscatalog.repositories.UserRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable) {
        Page<User> list = userRepository.findAll(pageable);
        return list.map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        User entity = getById(id);
        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(final UserInsertDTO InsertDTO) {
        validateIfExistsByEmail(InsertDTO);
        User entity = new User();
        updateData(entity, InsertDTO);
        entity.setPassword(passwordEncoder.encode(InsertDTO.getPassword()));
        userRepository.save(entity);
        return new UserDTO(entity);
    }


    @Transactional
    public UserDTO update(Long id, UserDTO UserDTO) {
        validateIfExistsByEmail(UserDTO);
        User entity = getById(id);
        updateData(entity, UserDTO);
        entity = userRepository.save(entity);
        return new UserDTO(entity);
    }

    //
    public void validateIfExistsByEmail(UserDTO user) {
        User byEmail = userRepository.findByEmail(user.getEmail());
        if (byEmail != null) {
            throw new EntityExistsException("User with email " + user.getEmail() + " already exists");
        }
    }

    @Transactional
    public void delete(Long id) {
        User entity = getById(id);
        userRepository.delete(entity);
    }


    private User getById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElseThrow(() -> new ResourceNotFoundException("id not found: " + id));
    }

    private void updateData(User user, UserDTO obj) {
        BeanUtils.copyProperties(obj, user, "id");

        user.getRoles().clear();
        for (RoleDTO roleDTO : obj.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDTO.getId());
            user.getRoles().add(role);
        }

    }

    @Transactional(readOnly = true)
    protected List<UserDetailsProjection> searchUserByUsername(String username) {
        return userRepository.searchUserAndRolesByEmail(username);
    }


    @Transactional
    public UserDetailsDTO loadUserByUsernameGet(String username) {
        List<UserDetailsProjection> projections = userRepository.searchUserAndRolesByEmail(username);
        if (projections.isEmpty()) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return new UserDetailsDTO(projections);
    }

    protected User authenticated() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return userRepository.findByEmail2(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));

        } catch (Exception e) {
            logger.error("Authentication error: ", e);
            throw new UsernameNotFoundException("User not found");
        }
    }

    @Transactional(readOnly = true)
    public UserLoggedDTO getMe() {
        User user = authenticated();
        return new UserLoggedDTO(user);
    }
}




