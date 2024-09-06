package com.devsuperior.dscatalog.dto.exception;

import com.devsuperior.dscatalog.dto.RoleDTO;
import com.devsuperior.dscatalog.entites.Role;
import com.devsuperior.dscatalog.projections.UserDetailsProjection;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDetailsDTO {

    private String email;
    private String password;
    private Set<RoleDTO> roles = new HashSet<>();

    public UserDetailsDTO() {
    }

    public UserDetailsDTO(String email, String password, Set<RoleDTO> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public UserDetailsDTO(List<UserDetailsProjection> projections) {
        if (projections != null && !projections.isEmpty()) {
            UserDetailsProjection firstProjection = projections.get(0);
            this.email = firstProjection.getUsername();
            this.password = firstProjection.getPassword();
            this.roles = projections.stream()
                    .map(projection -> new RoleDTO(
                            new Role(projection.getRoleId(), projection.getAuthority())))
                    .collect(Collectors.toSet());
        }
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDTO> roles) {
        this.roles = roles;
    }
}
