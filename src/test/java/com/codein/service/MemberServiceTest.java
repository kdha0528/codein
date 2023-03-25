package com.codein.service;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.Member;
import com.codein.domain.Session;
import com.codein.exception.NotExistsAccountException;
import com.codein.exception.NotSigninedAccount;
import com.codein.exception.Unauthorized;
import com.codein.repository.MemberRepository;
import com.codein.repository.SessionRepository;
import com.codein.request.MemberEdit;
import com.codein.request.Signin;
import com.codein.request.Signup;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.function.BiFunction;


@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private SessionRepository sessionRepository;


    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given

        PasswordEncoder passwordEncoder = new PasswordEncoder();

        Signup signup = Signup.builder()
                .name("데일리")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
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

    @Test
    @DisplayName("로그인 성공")
    void test2() {
        // given
        Signup signup = Signup.builder()
                .name("데일리")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();
        memberService.signup(signup);

        Signin signin = Signin.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();

        // when
        String accessToken = memberService.signin(signin);

        // then
        Assertions.assertNotNull(accessToken);
    }


    @Test
    @DisplayName("로그아웃 성공")
    void test3() {
        // given
        Signup signup = Signup.builder()
                .name("데일리")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();
        memberService.signup(signup);

        Signin signin = Signin.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        String accessToken = memberService.signin(signin);

        // when
        memberService.logout(accessToken);

        // then
        Member member = memberRepository.findByEmail("kdha4585@gmail.com")
                .orElseThrow(Unauthorized::new);

        BiFunction<String, Member, Session> findMemberSession = (t, m) -> {
            for (Session s : m.getSessions()) {
                if (s.getAccessToken().equals(t)) {
                    return s;
                }
            }
            return null;
        };

        Session memberSession = findMemberSession.apply(accessToken, member);

        Assertions.assertNull(memberSession);
    }

    @Test
    @DisplayName("회원정보 수정 성공")
    void test4() {
        // given
        Signup signup = Signup.builder()
                .name("데일리")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();
        memberService.signup(signup);

        Signin signin = Signin.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        String accessToken = memberService.signin(signin);

        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(NotSigninedAccount::new);

        Member member = session.getMember();

        MemberEdit memberEdit = MemberEdit.builder()
                .email("kdha0528@gmail.com")
                .phone("01044444444")
                .name(null)
                .password("11112222")
                .build();
        // when
        memberService.memberEdit(accessToken, memberEdit);

        // then
        Member editedMember = memberRepository.findByEmail(member.getEmail())
                .orElseThrow(NotExistsAccountException::new);
        Assertions.assertEquals(member.getName(), editedMember.getName());
        Assertions.assertEquals(member.getEmail(), editedMember.getEmail());
        Assertions.assertEquals(member.getPhone(), editedMember.getPhone());
        Assertions.assertEquals(member.getPassword(), editedMember.getPassword());
    }
}