package com.codein.responsedto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MemberListResponseDto {
    private final Long id;
    private final String email;
    private final String name;

    @Builder
    public MemberListResponseDto(Long id, String email, String name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }
}
