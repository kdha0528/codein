package com.codein.controller;


import com.codein.request.PageSize;
import com.codein.request.Signin;
import com.codein.request.Signup;
import com.codein.response.MemberResponse;
import com.codein.service.MemberService;
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

    @GetMapping("/")
    public List<MemberResponse> getMemberList(@ModelAttribute PageSize pageSize) {
        return memberService.getMemberList(pageSize);
    }

    @PostMapping("/signup")
    public void signup(@RequestBody @Valid Signup signup) {
        signup.validate();
        memberService.signup(signup);
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signin(@RequestBody Signin signin) {
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
}
