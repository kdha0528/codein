package com.codein.requestservicedto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginServiceDto {
    private final String email;
    private final String password;


    @Builder
    public LoginServiceDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
