package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.entites.Role;
import com.devsuperior.dscatalog.entites.User;
import com.devsuperior.dscatalog.projections.UserDetailsProjection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);


    private final UserService userService;

    public LoginService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserDetailsProjection> result = userService.searchUserByUsername(username);
        if (result.isEmpty()) {
            logger.error("User not found{}", username);
            throw new UsernameNotFoundException("username not found: " + username);
        }
        User user = new User();
        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());

        for (UserDetailsProjection projection : result) {
            user.addRoles(new Role(projection.getRoleId(), projection.getAuthority()));
        }

        logger.info("User found{}", username);
        return user;
    }


}
