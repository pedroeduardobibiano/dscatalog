package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.dto.UserDTO;
import com.devsuperior.dscatalog.entites.User;
import com.devsuperior.dscatalog.projections.UserDetailsProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    @Query(value = """
                SELECT tb_user.email AS username, tb_user.password, tb_role.id AS roleId,
                       tb_role.authority FROM tb_user
                INNER JOIN tb_user_role ON tb_user.id = tb_user_role.user_id
                INNER JOIN tb_role ON tb_role.id = tb_user_role.role_id
                WHERE tb_user.email = :email
            """, nativeQuery = true)
    List<UserDetailsProjection> searchUserAndRolesByEmail(String email);


    @Query(value = "SELECT * FROM tb_user WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail2(String email);


}
