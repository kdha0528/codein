package com.rainbowflavor.hdcweb.repository;

import com.rainbowflavor.hdcweb.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRoleRepository extends JpaRepository<UserRole,Long>{
}
