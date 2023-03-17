package com.codein.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String name;

    private String tel;

    private LocalDate birth;

    private String sex;

    private LocalDateTime createdAt;

    private Long point;

    @Builder
    public Member(String email, String password, String name, String tel, LocalDate birth, String sex) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.tel = tel;
        this.birth = birth;
        this.sex = sex;
        this.createdAt = LocalDateTime.now();
        this.point = 0L;
    }
}
