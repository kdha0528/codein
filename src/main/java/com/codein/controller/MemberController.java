package com.codein.controller;


import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.member.Member;
import com.codein.domain.member.Role;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.SessionRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.PageSizeDto;
import com.codein.requestdto.member.*;
import com.codein.responsedto.LoginResponseDto;
import com.codein.responsedto.ProfileResponseDto;
import com.codein.responsedto.ProfileSettingsResponseDto;
import com.codein.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final SessionRepository sessionRepository;
    private final MemberService memberService;

    @GetMapping(value = {"/home", "/", "/index"})
    public List<LoginResponseDto> getMemberList(@ModelAttribute PageSizeDto pageSizeDto, RedirectAttributes redirect, HttpServletResponse response) {
        List<LoginResponseDto> members = memberService.getMemberList(pageSizeDto);
        return members;
    }

    @PostMapping("/signup")
    public String signup(@RequestBody @Valid SignupDto signupDto) {
        memberService.signup(signupDto.toSignupServiceDto());
        return "redirect:/home";
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        String accessToken = memberService.login(loginDto.toMemberServiceDto());
        response.addHeader(HttpHeaders.SET_COOKIE, memberService.buildResponseCookie(accessToken).toString());
        response.addHeader(HttpHeaders.AUTHORIZATION, accessToken);
        return memberRepository.findByAccessToken(accessToken).toMemberResponse();
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/logout")
    public String logout(@CookieValue(value = "accesstoken") Cookie cookie) {
        memberService.logout(cookie.getValue());
        return "redirect:/home";
    }

    @MySecured(role = Role.MEMBER)
    @GetMapping("/members/{id}")
    public ProfileResponseDto getProfile(@PathVariable Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotExistsException::new);
        return member.toProfileResponse();
    }

    @MySecured(role = Role.MEMBER)
    @GetMapping(value = "/settings/profile")
    public ProfileSettingsResponseDto getProfileSettings(@CookieValue(value = "accesstoken") Cookie cookie) throws IOException {
        Member member = memberRepository.findByAccessToken(cookie.getValue());
        if (member != null) {
            return member.toProfileSettingsResponse();
        } else {
            throw new MemberNotExistsException();
        }
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/settings/profile")
    public void editProfile(@CookieValue(value = "accesstoken") Cookie cookie, @ModelAttribute EditProfileDto editProfileDto) {
        memberService.editProfile(cookie.getValue(), editProfileDto.toEditProfileServiceDto());
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/settings/account/password")
    public void changePassword(@CookieValue(value = "accesstoken") Cookie cookie, @RequestBody @Valid PasswordDto passwordDto) {
        memberService.changePassword(cookie.getValue(), passwordDto.toPasswordServiceDto());
        memberService.logout(cookie.getValue());
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/settings/account/email")
    public void changeEmail(@CookieValue(value = "accesstoken") Cookie cookie, @RequestBody @Valid EmailDto emailDto) {
        memberService.changeEmail(cookie.getValue(), emailDto.toEmailServiceDto());
        memberService.logout(cookie.getValue());
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/settings/account/phone")
    public void changePhone(@CookieValue(value = "accesstoken") Cookie cookie, @RequestBody @Valid PhoneDto phoneDto) {
        memberService.changePhone(cookie.getValue(), phoneDto.toPhoneServiceDto());
        memberService.logout(cookie.getValue());
    }

    @MySecured(role = Role.MEMBER)
    @DeleteMapping("/settings/account")
    public String deleteMember(@CookieValue(value = "accesstoken") Cookie cookie, HttpServletResponse response) {
        memberService.deleteMember(cookie.getValue());
        return "redirect:/home";
    }
}
