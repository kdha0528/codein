package com.rainbowflavor.hdcweb.repository;

import com.rainbowflavor.hdcweb.domain.ERole;
import com.rainbowflavor.hdcweb.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaRoleRepository extends JpaRepository<Role, Long> {
    Role findByName(ERole eRole);
}