package com.codein.controller;


import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.Role;
import com.codein.repository.MemberRepositoryCustom;
import com.codein.repository.SessionRepository;
import com.codein.requestdto.EditMemberDto;
import com.codein.requestdto.LoginDto;
import com.codein.requestdto.PageSizeDto;
import com.codein.requestdto.SignupDto;
import com.codein.responsedto.LoginResponseDto;
import com.codein.responsedto.ProfileResponseDto;
import com.codein.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepositoryCustom memberRepository;
    private final SessionRepository sessionRepository;

    private final MemberService memberService;

    @GetMapping("/home")
    public List<LoginResponseDto> getMemberList(@ModelAttribute PageSizeDto pageSizeDto, RedirectAttributes redirect, HttpServletResponse response) {
        List<LoginResponseDto> members = memberService.getMemberList(pageSizeDto);
        return members;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody @Valid SignupDto signupDto) {
        memberService.signup(signupDto.toEntity());
        return "redirect:/home";
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        String accessToken = memberService.login(loginDto.toMemberServiceDto());
        response.addHeader(HttpHeaders.SET_COOKIE, memberService.buildResponseCookie(accessToken).toString());
        response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        return memberRepository.findByAccessToken(accessToken).changeMemberResponse();
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/logout")
    public String logout(@CookieValue(value = "accesstoken") Cookie cookie) {
        memberService.logout(cookie.getValue());
        return "redirect:/home";
    }

    @MySecured(role = Role.MEMBER)
    @GetMapping("/getprofile")
    public ProfileResponseDto getProfile(@CookieValue(value = "accesstoken") Cookie cookie) {
        return memberRepository.findByAccessToken(cookie.getValue()).changeProfileResponse();
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/editmember")
    public String editMember(@CookieValue(value = "accesstoken") Cookie cookie, @RequestBody @Valid EditMemberDto editMemberDto, HttpServletResponse response) {
        memberService.editMember(cookie.getValue(), editMemberDto.toEditMemberServiceDto());
        memberService.logout(cookie.getValue());
        return "redirect:/home";
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/deletemember")
    public String deleteMember(@CookieValue(value = "accesstoken") Cookie cookie, HttpServletResponse response) {
        memberService.deleteMember(cookie.getValue());
        return "redirect:/home";
    }
}
