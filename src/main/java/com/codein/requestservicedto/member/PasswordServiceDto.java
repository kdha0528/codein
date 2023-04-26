package com.codein.requestservicedto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PasswordServiceDto {
    private final String originPassword;
    private final String newPassword;
    @Builder
    public PasswordServiceDto(String originPassword, String newPassword) {
        this.originPassword = originPassword;
        this.newPassword = newPassword;
    }
}
