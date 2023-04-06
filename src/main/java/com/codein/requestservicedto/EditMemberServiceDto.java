package com.codein.requestservicedto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EditMemberServiceDto {

    private final String email;
    private final String password;
    private final String name;
    private final String nickname;
    private final String phone;

    @Builder
    public EditMemberServiceDto(String email, String password, String name, String nickname, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
    }
}
