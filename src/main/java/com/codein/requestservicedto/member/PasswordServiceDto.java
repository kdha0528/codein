package com.codein.requestservicedto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PasswordServiceDto {
    private final String password;

    @Builder
    public PasswordServiceDto(String password) {
        this.password = password;
    }
}
