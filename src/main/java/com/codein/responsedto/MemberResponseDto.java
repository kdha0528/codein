package com.codein.responsedto;

import com.codein.domain.Member;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberResponseDto {

    private Long id;

    private String email;

    private String name;


    // 생성자 오버로딩
    public MemberResponseDto(Member member) {
        this.id = member.getId();
        this.email = member.getEmail();
        this.name = member.getName();
    }

    @Builder
    public MemberResponseDto(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

}
