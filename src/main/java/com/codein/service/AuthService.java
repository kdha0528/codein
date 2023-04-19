package com.codein.service;

import com.codein.domain.auth.Token;
import com.codein.domain.member.Member;
import com.codein.error.exception.auth.InvalidRefreshTokenException;
import com.codein.repository.TokenRepository;
import com.codein.repository.member.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokenRepository tokenRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ArrayList<String> validateRefreshToken(String refreshToken) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(InvalidRefreshTokenException::new);

        Member member = memberRepository.findByAccessToken(token.getRefreshToken());

        tokenRepository.delete(token);

        Token newToken = Token.builder()
                .member(member)
                .build();

        ArrayList<String> tokens = new ArrayList<>();
        tokens.add(newToken.getRefreshToken());
        tokens.add(newToken.getAccessToken());
        return tokens;

    }

    @Transactional
    public ResponseCookie RefreshTokenToCookie(String token) {
        return ResponseCookie.from("refreshtoken", token)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofDays(14L))
                .sameSite("Strict")
                .domain(".loca.lt")
                .build();
    }

    @Transactional
    public ResponseCookie AccessTokenToCookie(String token) {
        return ResponseCookie.from("accesstoken", token)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofMinutes(30))
                .sameSite("Strict")
                .domain(".loca.lt")
                .build();
    }

}
