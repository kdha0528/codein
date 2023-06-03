package com.codein.controller;

import com.codein.error.exception.auth.RefreshTokenNullException;
import com.codein.repository.TokensRepository;
import com.codein.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final TokensRepository tokensRepository;

    private final AuthService authService;

    @PostMapping("/refreshtoken")
    public void validateRefreshToken(@CookieValue(value = "refreshtoken") Cookie cookie, HttpServletResponse response) {
        if (cookie == null) {
            throw new RefreshTokenNullException();
        } else {
            ArrayList<String> tokens = authService.validateRefreshToken(cookie.getValue());
            if(tokens == null){ // refresh token이 정상적이지 않은 경우
                response.addHeader(HttpHeaders.SET_COOKIE,authService.deleteCookie("refreshtoken").toString());
                return;
            }

            ResponseCookie refreshCookie = authService.RefreshTokenToCookie(tokens.get(0));
            ResponseCookie accessCookie = authService.AccessTokenToCookie(tokens.get(1));
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        }
    }
}
