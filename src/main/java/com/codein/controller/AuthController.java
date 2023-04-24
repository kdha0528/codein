package com.codein.controller;

import com.codein.error.exception.auth.RefreshTokenNullException;
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

    private final AuthService authService;

    @PostMapping("/refreshtoken")
    public void validateRefreshToken(@CookieValue(value = "refreshtoken") Cookie cookie, HttpServletResponse response) {
        if (cookie == null) {
            throw new RefreshTokenNullException();
        } else {
            ArrayList<String> tokens = authService.validateRefreshToken(cookie.getValue());
            ResponseCookie accessCookie = authService.RefreshTokenToCookie(tokens.get(0));
            ResponseCookie refreshCookie = authService.AccessTokenToCookie(tokens.get(1));
            response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
            response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        }
    }
}
