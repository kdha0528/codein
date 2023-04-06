package com.codein.requestdto;

import com.codein.requestservicedto.LoginServiceDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginDto {

    @Email(message = "이메일 형식을 맞춰주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private final String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "비밀번호는 소문자, 대문자, 숫자를 이용하여 8~20글자 입력해주세요.")
    private final String password;

    @Builder
    public LoginDto(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public LoginServiceDto toMemberServiceDto() {
        return LoginServiceDto.builder()
                .email(this.email)
                .password(this.password)
                .build();
    }
}
