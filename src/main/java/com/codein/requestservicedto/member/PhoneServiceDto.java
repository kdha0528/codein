package com.codein.requestservicedto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class PhoneServiceDto {

    private String phone;

    @Builder
    public PhoneServiceDto(String phone) {
        this.phone = phone;
    }
}
