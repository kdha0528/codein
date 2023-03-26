package com.codein.controller;


import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.Role;
import com.codein.request.MemberEdit;
import com.codein.request.PageSize;
import com.codein.request.Signin;
import com.codein.request.Signup;
import com.codein.response.MemberResponse;
import com.codein.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/home")
    public List<MemberResponse> getMemberList(@ModelAttribute PageSize pageSize) {
        return memberService.getMemberList(pageSize);
    }

    @PostMapping("/signup")
    public void signup(@RequestBody @Valid Signup signup) {
        memberService.signup(signup);
    }


    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@RequestBody @Valid Signin signin) {
        String accessToken = memberService.signin(signin);

        ResponseCookie responseCookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .maxAge(Duration.ofDays(30))
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .build();
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/logout")
    public String logout(@CookieValue(value = "SESSION") Cookie cookie) {
        memberService.logout(cookie.getValue());
        return "redirect:/home";
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/memberedit")
    public void memberEdit(@CookieValue(value = "SESSION") Cookie cookie, @RequestBody @Valid MemberEdit memberEdit) {

        memberService.memberEdit(cookie.getValue(), memberEdit);
    }
}