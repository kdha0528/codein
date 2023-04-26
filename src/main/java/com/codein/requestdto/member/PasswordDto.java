package com.codein.requestdto.member;

import com.codein.requestservicedto.member.PasswordServiceDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
public class PasswordDto {

    @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "비밀번호는 소문자, 대문자, 숫자를 이용하여 8~20글자 입력해주세요.")
    @NotBlank(message = "기존 비밀번호를 입력해주세요.")
    private final String originPassword;

    @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "비밀번호는 소문자, 대문자, 숫자를 이용하여 8~20글자 입력해주세요.")
    @NotBlank(message = "새 비밀번호를 입력해주세요.")
    private final String newPassword;

    @Builder
    public PasswordDto(String originPassword, String newPassword) {
        this.originPassword = originPassword;
        this.newPassword = newPassword;
    }

    public PasswordServiceDto toPasswordServiceDto() {
        return PasswordServiceDto.builder()
                .originPassword(this.originPassword)
                .newPassword(this.newPassword)
                .build();
    }
}
