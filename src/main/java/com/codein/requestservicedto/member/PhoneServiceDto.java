package com.codein.requestservicedto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PhoneServiceDto {

    private final String phone;

    @Builder
    public PhoneServiceDto(String phone) {
        this.phone = phone;
    }
}
