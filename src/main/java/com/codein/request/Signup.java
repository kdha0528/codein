package com.codein.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Signup {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식을 맞춰주세요.")
    private String email;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "비밀번호는 소문자, 대문자, 숫자를 이용하여 8~20글자 입력해주세요.")
    private String password;
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 10)
    private String name;
    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phone;
    @NotBlank(message = "생년월일을 입력해주세요.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "올바른 형식으로 입력해주세요.")
    private String birth;
    @NotBlank(message = "성별을 입력해주세요.")
    @Pattern(regexp = "^(?:male|female)$", message = "올바른 형식으로 입력해주세요.")
    private String sex;

    @Builder
    public Signup(String email, String password, String name, String phone, String birth, String sex) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
        this.sex = sex;
    }

}
