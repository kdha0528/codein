package com.codein.requestservicedto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EditMemberServiceDto {

    private String email;
    private String password;
    private String name;
    private String nickname;
    private String phone;

    @Builder
    public EditMemberServiceDto(String email, String password, String name, String nickname, String phone) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
    }
}
