package com.codein.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Signup {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    @NotBlank
    private String birth;
    @NotBlank
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

    public void validate() {
        // 회원가입 절차에서 검증해야 할 기능들 만들어두기
    }
}
