package com.codein.controller;


import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.Role;
import com.codein.request.Login;
import com.codein.request.MemberEdit;
import com.codein.request.PageSize;
import com.codein.request.Signup;
import com.codein.response.MemberResponse;
import com.codein.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/home")
    public String getMemberList(@ModelAttribute PageSize pageSize, RedirectAttributes redirect) {
        List<MemberResponse> list = memberService.getMemberList(pageSize);
        redirect.addFlashAttribute("list", list); // addFlashAttribute 는 휘발성, addAttribute는 새로고침해도 안사라짐

        return "redirect:/home";
    }

    @PostMapping("/signup")
    public String signup(@RequestBody @Valid Signup signup) {

        memberService.signup(signup);
        return "redirect:/home";
    }


    @PostMapping("/login")
    public String login(@RequestBody @Valid Login login, HttpServletResponse response) {
        String accessToken = memberService.login(login);

        ResponseCookie responseCookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .sameSite("Strict")
                .maxAge(Duration.ofDays(30))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
        return "redirect:/home";
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/logout")
    public String logout(@CookieValue(value = "SESSION") Cookie cookie) {
        memberService.logout(cookie.getValue());
        return "redirect:/home";
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/memberedit")
    public String memberEdit(@CookieValue(value = "SESSION") Cookie cookie, @RequestBody @Valid MemberEdit memberEdit) {
        memberService.memberEdit(cookie.getValue(), memberEdit);
        return "redirect:/logout";
    }

}
