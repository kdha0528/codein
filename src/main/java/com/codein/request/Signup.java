package com.codein.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class Signup {
    private String email;

    private String password;

    private String name;

    private String tel;

    private LocalDate birth;

    private String sex;
}
