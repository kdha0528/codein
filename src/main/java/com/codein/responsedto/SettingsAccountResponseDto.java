package com.codein.responsedto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
public class SettingsAccountResponseDto {

    private final String email;
    private final String phone;

    @Builder
    public SettingsAccountResponseDto(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }
}
