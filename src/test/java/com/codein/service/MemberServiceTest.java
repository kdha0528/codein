package com.codein.service;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import com.codein.domain.member.follow.Follow;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.TokensRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.repository.profileimage.ProfileImageRepository;
import com.codein.requestdto.member.EditProfileDto;
import com.codein.requestdto.member.LoginDto;
import com.codein.requestdto.member.SignupDto;
import com.codein.requestservicedto.member.FollowServiceDto;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseCookie;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    ResourceLoader resourceLoader;
    @Autowired
    private ProfileImageRepository profileImageRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private TokensRepository tokensRepository;


    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    Member signupLogin() {
        SignupDto signupDto = SignupDto.builder()
                .name("김동하")
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build();
        Member member = memberService.signup(signupDto.toSignupServiceDto());

        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        memberService.login(loginDto.toMemberServiceDto());

        return member;
    }
    String getToken() {
        Member member = memberRepository.findByEmail("kdha4585@gmail.com");
        Tokens tokens = tokensRepository.findByMember(member)
                .orElseThrow(MemberNotExistsException::new);
        return tokens.getAccessToken();
    }

    @Test
    @DisplayName("회원가입 성공")
    void test1() {
        // given

        PasswordEncoder passwordEncoder = new PasswordEncoder();

        SignupDto signupDto = SignupDto.builder()
                .name("김동하")
                .nickname("데일이")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();

        // when
        memberService.signup(signupDto.toSignupServiceDto());


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
                .name("김동하")
                .nickname("데일이")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();
        memberService.signup(signupDto.toSignupServiceDto());

        LoginDto login = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();

        // when
        ArrayList<String> tokens = memberService.login(login.toMemberServiceDto());
        ResponseCookie refreshCookie = authService.AccessTokenToCookie(tokens.get(0));
        ResponseCookie accessCookie = authService.AccessTokenToCookie(tokens.get(1));

        // then
        Assertions.assertNotNull(refreshCookie);
        Assertions.assertNotNull(accessCookie);
    }


    @Test
    @DisplayName("로그아웃 성공")
    void test3() {
        // given
        SignupDto signupDto = SignupDto.builder()
                .name("김동하")
                .nickname("데일이")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();
        memberService.signup(signupDto.toSignupServiceDto());

        LoginDto login = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        ArrayList<String> tokens = memberService.login(login.toMemberServiceDto());
        Cookie cookie = new Cookie("accesstoken",tokens.get(1));
        // when
        memberService.logout(cookie);

        // then
        Member nullMember = memberRepository.findByAccessToken(tokens.get(1));
        Assertions.assertNull(nullMember);
    }


    @Test
    @DisplayName("회원탈퇴 성공")
    void test5() {
        // given
        SignupDto signupDto = SignupDto.builder()
                .name("김동하")
                .nickname("데일이")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();
        memberService.signup(signupDto.toSignupServiceDto());

        LoginDto login = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();

        ArrayList<String> tokens = memberService.login(login.toMemberServiceDto());

        // when
        memberService.deleteMember(tokens.get(1));

        // then
        Assertions.assertNull(memberRepository.findByEmail(signupDto.getEmail()));
    }

    @Test
    @DisplayName("프로필 수정 성공")
    void test6() throws IOException {
        // given
        SignupDto signupDto = SignupDto.builder()
                .name("김동하")
                .nickname("데일이")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();
        memberService.signup(signupDto.toSignupServiceDto());

        LoginDto login = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        ArrayList<String> tokens = memberService.login(login.toMemberServiceDto());

        File file = new File(new File("").getAbsolutePath() + "/src/main/resources/test/images/test.png");
        MultipartFile multipartFile = new MockMultipartFile("image", "test.png", "image/png", new FileInputStream(file));
        System.out.println("getContentType = " + multipartFile.getContentType());

        EditProfileDto editProfileDto = EditProfileDto.builder()
                .name("김복자")
                .nickname("데일이")
                .profileImage(multipartFile)
                .build();

        // when
        memberService.editProfile(tokens.get(1), editProfileDto.toEditProfileServiceDto());

        // then
        Member editedMember = memberRepository.findByEmail("kdha4585@gmail.com");
        Assertions.assertEquals(editProfileDto.getName(), editedMember.getName());
        Assertions.assertEquals(editProfileDto.getNickname(), editedMember.getNickname());
    }

    @Test
    @DisplayName("팔로우 성공")
    void test7() throws IOException {
        // given
        Member follower = signupLogin();
        SignupDto signupDto = SignupDto.builder()
                .name("김동하")
                .nickname("데일이2")
                .email("kdha0528@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("0101234457")
                .build();
        Member following = memberService.signup(signupDto.toSignupServiceDto());

        FollowServiceDto followServiceDto = FollowServiceDto.builder()
                .accessToken(getToken())
                .followingId(following.getId())
                .build();

        // when
        Follow follow = memberService.followMember(followServiceDto);

        // then
        Assertions.assertEquals(follower.getNickname(), follow.getFollower().getNickname());
        Assertions.assertEquals(signupDto.getNickname(), follow.getFollowing().getNickname());

    }


}