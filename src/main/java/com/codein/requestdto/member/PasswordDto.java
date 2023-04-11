package com.codein.requestdto.member;

import com.codein.requestservicedto.member.PasswordServiceDto;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PasswordDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "비밀번호는 소문자, 대문자, 숫자를 이용하여 8~20글자 입력해주세요.")
    private final String password;

    @Builder
    public PasswordDto(String password) {
        this.password = password;
    }

    public PasswordServiceDto toPasswordServiceDto() {
        return PasswordServiceDto.builder()
                .password(this.password)
                .build();
    }
}
