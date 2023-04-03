package com.codein.responsedto;

import com.codein.domain.Member;
import com.codein.domain.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponseDto {

    private Long id;
    private String email;
    private String nickname;
    private String name;
    private Long point;
    private String role;


    // 생성자 오버로딩
    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.name = member.getName();
        this.point = member.getPoint();
        this.role = member.getRole().getRole();
    }

    @Builder
    public MemberResponseDto(Long id, String email, String nickname, String name, Long point, Role role) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.point = point;
        this.role = role.getRole();
    }

}
