package com.codein.responsedto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class LoginResponseDto {

    private final Long id;
    private final String email;
    private final String nickname;
    private final Integer point;
    private final String role;

    @Builder
    public LoginResponseDto(Long id, String email, String nickname, Integer point, String role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.point = point;
        this.role = role;
    }

}
