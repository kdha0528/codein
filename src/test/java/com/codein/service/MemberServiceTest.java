package com.codein.service;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.Member;
import com.codein.repository.MemberRepository;
import com.codein.repository.SessionRepository;
import com.codein.requestdto.LoginDto;
import com.codein.requestdto.MemberEditDto;
import com.codein.requestdto.SignupDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given

        PasswordEncoder passwordEncoder = new PasswordEncoder();

        SignupDto signupDto = SignupDto.builder()
                .name("데일리")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();

        // when
        memberService.signup(signupDto.toEntity());


        // then
        Assertions.assertEquals(1, memberRepository.count());
        Member member = memberRepository.findByEmail(signupDto.getEmail());
        Assertions.assertEquals(signupDto.getEmail(), member.getEmail());
        Assertions.assertEquals(signupDto.getName(), member.getName());
        Assertions.assertTrue(passwordEncoder.matches(signupDto.getPassword(), member.getPassword()));
    }

    @Test
    @DisplayName("로그인 성공")
    void test2() {
        // given
        SignupDto signupDto = SignupDto.builder()
                .name("데일리")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();
        memberService.signup(signupDto.toEntity());

        LoginDto login = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();

        // when
        String accessToken = memberService.login(login.toMemberServiceDto());

        // then
        Assertions.assertNotNull(accessToken);
    }


    @Test
    @DisplayName("로그아웃 성공")
    void test3() {
        // given
        SignupDto signupDto = SignupDto.builder()
                .name("데일리")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();
        memberService.signup(signupDto.toEntity());

        LoginDto login = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        String accessToken = memberService.login(login.toMemberServiceDto());

        // when
        memberService.logout(accessToken);

        // then
        Member nullMember = memberRepository.findByAccessToken(accessToken);
        Assertions.assertNull(nullMember);
    }

    @Test
    @DisplayName("회원정보 수정 성공")
    void test4() {
        // given
        SignupDto signupDto = SignupDto.builder()
                .name("데일리")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();
        memberService.signup(signupDto.toEntity());

        LoginDto login = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        String accessToken = memberService.login(login.toMemberServiceDto());

        MemberEditDto memberEditDto = MemberEditDto.builder()
                .email("kdha0528@gmail.com")
                .phone("01044444444")
                .name(null)
                .password("11112222")
                .build();
        // when
        memberService.memberEdit(accessToken, memberEditDto.toMemberServiceDto());

        // then
        Member editedMember = memberRepository.findByEmail(memberEditDto.getEmail());
        Assertions.assertEquals(signupDto.getName(), editedMember.getName());
        Assertions.assertEquals(memberEditDto.getEmail(), editedMember.getEmail());
        Assertions.assertEquals(memberEditDto.getPhone(), editedMember.getPhone());
        Assertions.assertTrue(passwordEncoder.matches(memberEditDto.getPassword(), editedMember.getPassword()));
    }

   
}