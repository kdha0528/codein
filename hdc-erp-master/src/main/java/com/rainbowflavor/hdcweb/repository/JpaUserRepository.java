package com.rainbowflavor.hdcweb.repository;

import com.rainbowflavor.hdcweb.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface JpaUserRepository extends JpaRepository<User, Long>{

    Optional<User> findByName(String name);
    Optional<User> findByUsername(String username);
    User findByEmail(String email);
}
