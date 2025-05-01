package com.example.springJWT.repository;

import com.example.springJWT.entity.User_WC;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User_WC, Long> {

    Boolean existsByUsername(String username);

    User_WC findByUsername(String username);
}
