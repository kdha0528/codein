package com.codein.responsedto;

import com.codein.domain.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileResponseDto {


    private String email;
    private String name;
    private String nickname;
    private String phone;


    // 생성자 오버로딩
    public ProfileResponseDto(Member member) {
        this.email = member.getEmail();
        this.name = member.getName();
        this.nickname = member.getNickname();
        this.phone = member.getPhone();
    }

    @Builder
    public ProfileResponseDto(String email, String name, String nickname, String phone) {
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
    }

}
