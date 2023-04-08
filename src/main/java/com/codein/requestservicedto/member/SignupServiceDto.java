package com.codein.requestservicedto.member;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignupServiceDto {
    private final String email;
    private final String nickname;
    private final String password;
    private final String name;
    private final String phone;
    private final String birth;
    private final String sex;

    @Builder
    public SignupServiceDto(String email, String nickname, String password, String name, String phone, String birth, String sex) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
        this.sex = sex;
    }

    public Member toEntity() {
        PasswordEncoder passwordEncoder = new PasswordEncoder();
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(passwordEncoder.encrypt(password))
                .name(name)
                .phone(phone)
                .birth(birth)
                .sex(sex)
                .build();
    }

}

