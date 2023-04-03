package com.codein.requestdto;

import com.codein.requestservicedto.EditMemberServiceDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EditMemberDto {

    @Email(message = "이메일 형식을 맞춰주세요.")
    private String email;
    @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,8}$", message = "2자 이상, 16자 이하, 영어 또는 숫자 또는 한글로 입력해주세요.")
    private String nickname;
    @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "비밀번호는 소문자, 대문자, 숫자를 이용하여 8~20글자 입력해주세요.")
    private String password;
    @Size(min = 2, max = 10)
    private String name;
    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phone;

    @Builder
    public EditMemberDto(String email, String nickname, String password, String name, String phone) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.name = name;
        this.phone = phone;
    }

    public EditMemberServiceDto toMemberServiceDto() {
        return EditMemberServiceDto.builder()
                .email(this.email)
                .nickname(this.nickname)
                .password(this.password)
                .name(this.name)
                .phone(this.phone)
                .build();
    }
}
