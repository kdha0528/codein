package com.codein.responsedto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberProfileResponseDto {

    private final String email;
    private final String name;
    private final String nickname;
    private final String phone;

    @Builder
    public MemberProfileResponseDto(String email, String name, String nickname, String phone) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
    }

}