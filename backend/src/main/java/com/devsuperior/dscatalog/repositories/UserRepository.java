package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entites.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
