package com.codein.controller;


import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.member.Member;
import com.codein.domain.member.Role;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.article.GetActivitiesDto;
import com.codein.requestdto.member.*;
import com.codein.responsedto.article.ActivityListResponseDto;
import com.codein.responsedto.member.LoginResponseDto;
import com.codein.responsedto.member.SettingsAccountResponseDto;
import com.codein.responsedto.member.SettingsProfileResponseDto;
import com.codein.service.ArticleService;
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

@Slf4j
@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberRepository memberRepository;
    private final AuthService authService;
    private final MemberService memberService;
    private final ArticleService articleService;

    @PostMapping("/signup")
    public String signup(@RequestBody @Valid SignupDto signupDto) {
        memberService.signup(signupDto.toSignupServiceDto());
        return "redirect:/home";
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
        ArrayList<String> tokens = memberService.login(loginDto.toMemberServiceDto());
        response.addHeader(HttpHeaders.SET_COOKIE, authService.RefreshTokenToCookie(tokens.get(0)).toString());
        response.addHeader(HttpHeaders.SET_COOKIE, authService.AccessTokenToCookie(tokens.get(1)).toString());
        return memberRepository.findByAccessToken(tokens.get(1)).toLoginResponseDto();
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/logout")
    public String logout(@CookieValue(value = "accesstoken") Cookie cookie, HttpServletResponse response) {
        memberService.logout(cookie);
        response.addHeader(HttpHeaders.SET_COOKIE, authService.deleteCookie("accesstoken").toString());
        response.addHeader(HttpHeaders.SET_COOKIE, authService.deleteCookie("refreshtoken").toString());
        return "redirect:/home";
    }

    @GetMapping(value = {"/members/{id}","/members/{id}/{activity}"})
    public ActivityListResponseDto getActivityList(
            @PathVariable(value = "id") Long id,
            @PathVariable(value = "activity", required = false) String activity,
            @RequestParam(value = "page", required = false) Integer page,
            @ModelAttribute GetActivitiesDto getActivitiesDto
    ) {
        return articleService.getActivityList(getActivitiesDto.toGetActivitiesServiceDto());
    }

    @MySecured(role = Role.MEMBER)
    @GetMapping(value = "/settings/profile")
    public SettingsProfileResponseDto getSettingsProfile(@CookieValue(value = "accesstoken") Cookie cookie) {
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
    public SettingsAccountResponseDto getSettingsAccount(@CookieValue(value = "accesstoken") Cookie cookie) {
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
