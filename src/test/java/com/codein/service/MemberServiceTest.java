package com.codein.service;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.Member;
import com.codein.repository.MemberRepository;
import com.codein.request.Signup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;


@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;


    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given

        PasswordEncoder passwordEncoder = new PasswordEncoder();

        Signup signup = Signup.builder()
                .email("kdha4585@gmail.com")
                .password("1234")
                .name("데일이")
                .tel("01075444357")
                .birth(LocalDate.of(1996, 5, 28))
                .sex("남성")
                .build();

        // when
        memberService.signup(signup);


        // then
        Assertions.assertEquals(1, memberRepository.count());

        Member member = memberRepository.findAll().iterator().next();
        Assertions.assertEquals(signup.getEmail(), member.getEmail());
        Assertions.assertEquals(signup.getName(), member.getName());
        Assertions.assertTrue(passwordEncoder.matches(signup.getPassword(), member.getPassword()));
    }

}