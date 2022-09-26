package com.rainbowflavor.hdcweb.service;

import com.rainbowflavor.hdcweb.domain.User;
import com.rainbowflavor.hdcweb.repository.JpaRoleRepository;
import com.rainbowflavor.hdcweb.repository.JpaUserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TestServiceUser {

    @Autowired
    UserService userService;
    @Autowired
    JpaUserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("User and Role Insert")
    void join() {
/*
        Long userId = userService.joinUser(user);
        User findUser = userRepository.getOne(userId);

        assertThat(findUser.getEmail()).isEqualTo(user.getEmail());*/
    }
}