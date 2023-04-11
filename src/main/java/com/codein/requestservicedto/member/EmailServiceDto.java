package com.codein.requestservicedto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EmailServiceDto {

    private final String email;

    @Builder
    public EmailServiceDto(String email) {
        this.email = email;
    }
}
