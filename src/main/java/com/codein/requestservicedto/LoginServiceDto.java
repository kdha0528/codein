package com.codein.requestservicedto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LoginServiceDto {
    private String email;
    private String password;


    @Builder
    public LoginServiceDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
