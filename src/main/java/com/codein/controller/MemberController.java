package com.codein.controller;


import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.member.Member;
import com.codein.domain.member.Role;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.PageSizeDto;
import com.codein.requestdto.member.*;
import com.codein.responsedto.*;
import com.codein.service.AuthService;
import com.codein.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final MemberService memberService;

    @GetMapping(value = {"/home", "/", "/index"})
    public List<MemberListResponseDto> getMemberList(@ModelAttribute PageSizeDto pageSizeDto) {
        return memberService.getMemberList(pageSizeDto);
    }

    @PostMapping("/signup")
    public String signup(@RequestBody @Valid SignupDto signupDto) {
        memberService.signup(signupDto.toSignupServiceDto());
        return "redirect:/home";
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        ArrayList<String> tokens = memberService.login(loginDto.toMemberServiceDto());
        ResponseCookie accessCookie = authService.RefreshTokenToCookie(tokens.get(0));
        ResponseCookie refreshCookie = authService.AccessTokenToCookie(tokens.get(1));
        response.addHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, accessCookie.toString());
        return memberRepository.findByAccessToken(tokens.get(1)).toLoginResponseDto();
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/logout")
    public String logout(@CookieValue(value = "accesstoken") Cookie cookie, HttpServletResponse response) {
        memberService.logout(cookie);
        response.addCookie(authService.deleteCookie("accesstoken"));
        response.addCookie(authService.deleteCookie("refreshtoken"));
        return "redirect:/home";
    }

    @GetMapping("/members/{id}")
    public MemberResponseDto getMemberProfile(@PathVariable Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(MemberNotExistsException::new);
        return member.toMemberResponseDto();
    }

    @MySecured(role = Role.MEMBER)
    @GetMapping(value = "/settings/profile")
    public SettingsProfileResponseDto getSettingsProfile(@CookieValue(value = "accesstoken") Cookie cookie) throws IOException {
        Member member = memberRepository.findByAccessToken(cookie.getValue());
        if (member != null) {
            return member.toSettingsProfileResponseDto();
        } else {
            throw new MemberNotExistsException();
        }
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping(value ="/settings/profile")
    public void editProfile(@CookieValue(value = "accesstoken") Cookie cookie, @ModelAttribute @Valid EditProfileDto editProfileDto) {
        memberService.editProfile(cookie.getValue(), editProfileDto.toEditProfileServiceDto());
    }

    @MySecured(role = Role.MEMBER)
    @GetMapping(value = "/settings/account")
    public SettingsAccountResponseDto getSettingsAccount(@CookieValue(value = "accesstoken") Cookie cookie) throws IOException {
        Member member = memberRepository.findByAccessToken(cookie.getValue());
        if (member != null) {
            return member.toSettingsAccountResponseDto();
        } else {
            throw new MemberNotExistsException();
        }
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/settings/account/password")
    public String changePassword(@CookieValue(value = "accesstoken") Cookie cookie, @RequestBody @Valid PasswordDto passwordDto) {
        memberService.changePassword(cookie.getValue(), passwordDto.toPasswordServiceDto());
        return "redirect:/logout";
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/settings/account/email")
    public String changeEmail(@CookieValue(value = "accesstoken") Cookie cookie,  @RequestBody @Valid EmailDto emailDto) {
        System.out.println("email dto = " + emailDto.getEmail());
        memberService.changeEmail(cookie.getValue(), emailDto.toEmailServiceDto());
        return "redirect:/logout";
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/settings/account/phone")
    public String changePhone(@CookieValue(value = "accesstoken") Cookie cookie, @RequestBody @Valid PhoneDto phoneDto) {
        memberService.changePhone(cookie.getValue(), phoneDto.toPhoneServiceDto());
        return "redirect:/logout";
    }

    @MySecured(role = Role.MEMBER)
    @DeleteMapping("/settings/account")
    public String deleteMember(@CookieValue(value = "accesstoken") Cookie cookie) {
        memberService.deleteMember(cookie.getValue());
        return "redirect:/logout";
    }
}
