package com.codein.requestdto.member;

import com.codein.requestservicedto.member.PhoneServiceDto;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PhoneDto {

    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private final String phone;

    @Builder
    public PhoneDto(String phone) {
        this.phone = phone;
    }

    public PhoneServiceDto toPhoneServiceDto() {
        return PhoneServiceDto.builder()
                .phone(this.phone)
                .build();
    }
}
