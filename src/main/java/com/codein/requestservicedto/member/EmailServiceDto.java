package com.codein.requestservicedto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class EmailServiceDto {

    private String email;

    @Builder
    public EmailServiceDto(String email) {
        this.email = email;
    }
}
