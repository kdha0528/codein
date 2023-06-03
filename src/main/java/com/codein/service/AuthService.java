package com.codein.service;

import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import com.codein.error.exception.auth.InvalidRefreshTokenException;
import com.codein.error.exception.auth.RefreshTokenNullException;
import com.codein.repository.TokensRepository;
import com.codein.repository.member.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final TokensRepository tokensRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ArrayList<String> validateRefreshToken(String refreshToken) {
        Tokens token = tokensRepository.findByRefreshToken(refreshToken)
                .orElseThrow(InvalidRefreshTokenException::new);

        Member member = memberRepository.findByRefreshToken(token.getRefreshToken());

        tokensRepository.delete(token);

        if(member == null){
            return null;
        }
        else {
            Tokens newTokens = Tokens.builder()
                    .member(member)
                    .build();

            ArrayList<String> tokens = new ArrayList<>();
            tokens.add(newTokens.getRefreshToken());
            tokens.add(newTokens.getAccessToken());
            return tokens;
        }
    }

    @Transactional
    public ResponseCookie RefreshTokenToCookie(String token) {
        return ResponseCookie.from("refreshtoken", token)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofDays(1L))
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
                .maxAge(Duration.ofMinutes(1L))
                .sameSite("Strict")
                .domain(".loca.lt")
                .build();
    }

    @Transactional
    public ResponseCookie deleteCookie(String tokenName) {
        return ResponseCookie.from(tokenName, "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .sameSite("Strict")
                .domain(".loca.lt")
                .build();
    }

    @Transactional
    public void deleteTokens(String refreshToken) {
        Tokens tokens = tokensRepository.findByRefreshToken(refreshToken)
                .orElseThrow(RefreshTokenNullException::new);

        tokensRepository.delete(tokens);
    }

}
