package com.codein.domain.auth;

import com.codein.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String refreshToken;
    @NotNull
    private String accessToken;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder
    public Token(Member member) {
        this.refreshToken = UUID.randomUUID().toString();
        this.accessToken = UUID.randomUUID().toString();
        this.member = member;
        member.getTokens().add(this);
    }

    public Member getMember() {
        return member;
    }

}
