package com.codein.service;

import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import com.codein.error.exception.auth.InvalidRefreshTokenException;
import com.codein.error.exception.auth.RefreshTokenNullException;
import com.codein.repository.tokens.TokensRepository;
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
    private final TokensRepository tokensRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public ArrayList<String> validateRefreshToken(String refreshToken) {
        Tokens token = tokensRepository.findByRefreshToken(refreshToken);
        if(token == null){  // db에 일치하는 refresh token이 없는 경우 쿠키를 삭제해야하기 때문에 null을 보내 쿠키를 삭제 후 예외처리
            System.out.println("refresh token으로 token을 찾지 못함");
            return null;
        } else {

            Member member = memberRepository.findByRefreshToken(token.getRefreshToken());
            tokensRepository.delete(token);
            if (member == null) { // db에 refresh token을 소유하고 있는 member가 존재하지 않는 경우 null을 보내 refresh token 삭제
                System.out.println("token으로 member를 찾지 못함");
                return null;
            } else {

                Tokens newTokens = Tokens.builder()
                        .member(member)
                        .build();

                ArrayList<String> tokens = new ArrayList<>();
                tokens.add(newTokens.getRefreshToken());
                tokens.add(newTokens.getAccessToken());
                return tokens;
            }
        }
    }

    @Transactional
    public ResponseCookie RefreshTokenToCookie(String token) {
        return ResponseCookie.from("refreshtoken", token)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofDays(1L))
                .sameSite("None")
                .domain(".code-in.site")
                .build();
    }

    @Transactional
    public ResponseCookie AccessTokenToCookie(String token) {
        return ResponseCookie.from("accesstoken", token)
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofMinutes(30L))
                .sameSite("None")
                .domain(".code-in.site")
                .build();
    }

    @Transactional
    public ResponseCookie deleteCookie(String tokenName) {
        return ResponseCookie.from(tokenName, "")
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(0)
                .sameSite("None")
                .domain(".code-in.site")
                .build();
    }

    @Transactional
    public void deleteTokensByRefreshToken(String refreshToken) {
        Tokens tokens = tokensRepository.findByRefreshToken(refreshToken);
        if(tokens != null){
            tokensRepository.delete(tokens);
        } else {
            throw new InvalidRefreshTokenException();
        }
    }

}
