package com.codein.controller;

import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import com.codein.domain.member.Role;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.TokensRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.repository.profileimage.ProfileImageRepository;
import com.codein.requestdto.article.Activity;
import com.codein.requestdto.member.*;
import com.codein.responsedto.SettingsAccountResponseDto;
import com.codein.service.ArticleService;
import com.codein.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.FileInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private MemberService memberService;
    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;
    @Autowired
    private ProfileImageRepository profileImageRepository;
    @Autowired
    private ArticleService articleService;

    public void deleteOrphanProfileImage() {
        File file = new File(uploadPath);
        if (file.exists()) {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                assert files != null;
                for (File value : files) {
                    if (profileImageRepository.findByName(value.getName()) == null) {
                        if (value.delete()) System.out.println(value.getName() + "삭제 성공");
                        else System.out.println(value.getName() + "삭제 실패");
                    }
                }
            }
        }
    }

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
        deleteOrphanProfileImage();
    }

    void signup() {
        SignupDto signupDto = SignupDto.builder()
                .name("김동하")
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build();
        memberService.signup(signupDto.toSignupServiceDto());
    }

    void login() {
        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        memberService.login(loginDto.toMemberServiceDto());
    }

    Cookie getCookie() {
        Member member = memberRepository.findByEmail("kdha4585@gmail.com");
        Tokens tokens = tokensRepository.findByMember(member)
                .orElseThrow(MemberNotExistsException::new);
        String token = tokens.getAccessToken();

        return new Cookie("accesstoken", token);
    }

    @Test
    @DisplayName("회원가입")
    void test1_1() throws Exception {
        //given
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585@gmail.com") // email 형식
                .password("123a1A34")   // 8~20 숫자, 소문자, 대문자
                .name("김동하")    // 2~10글자
                .nickname("데일이") // 한글, 영어, 숫자 2~16
                .phone("01075444357")   // 전화번호 형식 '-'제외
                .birth("1996-05-28")    // 날짜 형식
                .sex("male")    // male or female
                .build();
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signupDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("회원가입 실패 : 중복된 이메일")
    void test1_2() throws Exception {
        //given
        SignupDto signupDto1 = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .name("김동하")
                .nickname("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        mockMvc.perform(post("/signup")
                .content(objectMapper.writeValueAsString(signupDto1))
                .contentType(APPLICATION_JSON));

        SignupDto signupDto2 = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .name("김동하")
                .nickname("데일리")
                .phone("01044444444")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );

        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signupDto2))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("회원가입 실패 : 중복된 전화번호")
    void test1_3() throws Exception {
        //given
        SignupDto signupDto1 = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .name("김동하")
                .nickname("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        mockMvc.perform(post("/signup")
                .content(objectMapper.writeValueAsString(signupDto1))
                .contentType(APPLICATION_JSON));

        SignupDto signupDto2 = SignupDto.builder()
                .email("kdha1234@gmail.com")
                .password("12341234")
                .name("김동하")
                .nickname("데일리")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signupDto2))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("회원가입 실패 : 올바르지 않은 형식의 전화번호")
    void test1_4() throws Exception {
        //given
        SignupDto signupDto = SignupDto.builder()
                .email("kdha1234@gmail.com")
                .password("12341234")
                .name("김동하")
                .nickname("데일이")
                .phone("010-7544-4357") // "-" 없어야됨
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signupDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("회원가입 실패 : 비밀번호 특수문자 사용")
    void test1_5() throws Exception {
        //given
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234@@")
                .name("김동하")
                .nickname("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signupDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("회원가입 실패 : 이메일 형식 불일치")
    void test1_6() throws Exception {
        //given
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585")
                .password("12341234")
                .name("김동하")
                .nickname("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signupDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("회원가입 실패 : 비밀번호 글자 수 초과")
    void test1_7() throws Exception {
        //given
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .password("1234123412351235123512351235123") // 20글자 까지
                .name("김동하")
                .nickname("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signupDto))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("로그인 성공")
    void test2_1() throws Exception {
        //given
        signup();

        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();

        // expected
        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 : 비밀번호 불일치")
    void test2_2() throws Exception {
        //given
        signup();

        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12345678")
                .build();

        // expected
        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 : 존재하지 않는 이메일")
    void test2_3() throws Exception {
        //given
        signup();

        LoginDto loginDto = LoginDto.builder()
                .email("kdha0528@gmail.com")
                .password("12341234")
                .build();

        // expected
        mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void test3_1() throws Exception {
        //given
        signup();
        login();

        // expected
        mockMvc.perform(post("/logout").cookie(getCookie()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("redirect:/home"))
                .andDo(print());
    }

    @Test
    @DisplayName("로그아웃 실패 : 권한 없음")
    void test3_2() throws Exception {
        //given
        signup();
        login();
        Member member = memberRepository.findByEmail("kdha4585@gmail.com");
        member.setRole(Role.NONE);   // 강제로 role 삭제
        memberRepository.save(member);

        // expected
        mockMvc.perform(post("/logout").cookie(getCookie()))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("회원탈퇴 성공")
    void test5_1() throws Exception {
        // given
        signup();
        login();

        // expected
        mockMvc.perform(delete("/settings/account").cookie(getCookie()))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("redirect:/logout"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원탈퇴 실패 : 권한 없음")
    void test5_2() throws Exception {
        // given
        signup();
        login();

        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        member.setRole(Role.NONE);
        memberRepository.save(member);

        // expected
        mockMvc.perform(delete("/settings/account").cookie(getCookie()))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("프로필 가져오기: 성공")
    void Test6_1() throws Exception {
        // given
        signup();
        login();
        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        Long memberId = member.getId();

        // expected
        mockMvc.perform(get("/members/{memberId}", memberId).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("프로필 수정: 이미지가 없는 경우 성공")
    void Test7_1() throws Exception {
        // given
        signup();
        login();

        // expected
        mockMvc.perform(multipart("/settings/profile")
                        .param("name","김복자")
                        .param("nickname","데일이")
                        .cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("프로필 수정: 이미지가 있는 경우 성공")
    void Test7_2() throws Exception {
        // given
        signup();
        login();

        File file = new File(new File("").getAbsolutePath() + "/src/main/resources/test/images/test.png");
        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "test.png", "image/png", new FileInputStream(file.getPath()));

        // expected
        mockMvc.perform(multipart("/settings/profile")
                        .file(profileImage)
                        .param("name","김복자")
                        .param("nickname","데일이")
                        .cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("프로필 수정 실패: 사진 용량 초과")
    void Test7_3() throws Exception {
        // given
        signup();
        login();

        File file = new File(new File("").getAbsolutePath() + "/src/main/resources/test/images/test2.png");
        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "test2.png", "image/png", new FileInputStream(file.getPath()));

        // expected
        mockMvc.perform(multipart("/settings/profile")
                        .file(profileImage)
                        .param("name","김복자")
                        .param("nickname","데일이")
                        .cookie(getCookie()))
                .andExpect(status().is4xxClientError())
                .andDo(print());

    }

    @Test
    @DisplayName("프로필 수정 성공: 사진 변경 시 기존 사진 삭제")
    void Test7_4() throws Exception {
        // given
        signup();
        login();

        File file = new File(new File("").getAbsolutePath() + "/src/main/resources/test/images/test.png");
        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "test.png", "image/png", new FileInputStream(file.getPath()));

        mockMvc.perform(multipart("/settings/profile")
                        .file(profileImage)
                        .param("name","김복자")
                        .param("nickname","데일이")
                        .cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());

        File file2 = new File(new File("").getAbsolutePath() + "/src/main/resources/test/images/test3.jpg");
        MockMultipartFile profileImage2 = new MockMultipartFile("profileImage", "test3.jpg", "image/jpg", new FileInputStream(file2.getPath()));

        // expected
        mockMvc.perform(multipart("/settings/profile")
                        .file(profileImage2)
                        .param("name","김복자")
                        .param("nickname","데일이")
                        .cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("프로필 수정 페이지 정보 가져오기 성공: 이미지 있을 때")
    @Transactional
    void Test7_5() throws Exception {
        // given
        signup();
        login();

        File file = new File(new File("").getAbsolutePath() + "/src/main/resources/test/images/test.png");
        MockMultipartFile profileImage = new MockMultipartFile("profileImage", "test.png", "image/png", new FileInputStream(file.getPath()));

        mockMvc.perform(multipart("/settings/profile")
                        .file(profileImage)
                        .param("name","김복자")
                        .param("nickname","데일이")
                        .cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());

        // expected
        Member member = memberRepository.findByEmail("kdha4585@gmail.com");
        String imagePath = "/my-backend-api/images/profile/" + member.getProfileImage().getImgFileName();
        assertEquals(member.toSettingsProfileResponseDto().getImagePath(), imagePath);

    }

    @Test
    @DisplayName("프로필 수정 페이지 정보 가져오기 성공: 이미지 없을 때")
    void Test7_6() throws Exception {
        // given
        signup();
        login();

        mockMvc.perform(multipart("/settings/profile")
                        .param("name","김복자")
                        .param("nickname","데일이")
                        .cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());

        // expected
        mockMvc.perform(get("/settings/profile")
                        .cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void test8_1() throws Exception {
        // given
        signup();
        login();

        PasswordDto passwordDto = PasswordDto.builder()
                .originPassword("12341234")
                .newPassword("11112222")
                .build();

        System.out.println(memberRepository.findByAccessToken(getCookie().getValue()).getPassword());
        // expected
        mockMvc.perform(post("/settings/account/password").cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordDto))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("이메일 변경 성공")
    void test9_1() throws Exception {
        // given
        signup();
        login();

        EmailDto emailDto = EmailDto.builder()
                .email("kdha0528@gmail.com")
                .build();

        // expected
        mockMvc.perform(post("/settings/account/email").cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emailDto))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("전화번호 변경 성공")
    void test10_1() throws Exception {
        // given
        signup();
        login();

        PhoneDto phoneDto = PhoneDto.builder()
                .phone("01011112222")
                .build();

        // expected
        mockMvc.perform(post("/settings/account/phone").cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(phoneDto))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("Settings Account 정보 가져오기 성공")
    void test11_1() throws Exception {
        // given
        signup();
        login();
        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        SettingsAccountResponseDto settingsAccountResponseDto = SettingsAccountResponseDto.builder()
                .email(member.getEmail())
                .phone(member.getPhone())
                .build();

        // expected
        mockMvc.perform(get("/settings/account").cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(MockMvcResultMatchers.content().json(objectMapper.writeValueAsString(settingsAccountResponseDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("멤버 활동내역 가져오기 성공")
    void test12_1() throws Exception {
        // given
        signup();
        login();
        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        articleService.createDummies(member);

        // expected
        mockMvc.perform(get("/members/{id}",member.getId()).cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("멤버 활동내역 가져오기 성공: activity 설정")
    void test12_2() throws Exception {
        // given
        signup();
        login();
        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        articleService.createDummies(member);

        // expected
        mockMvc.perform(get("/members/{id}/{activity}",member.getId(), Activity.ARTICLES).cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("멤버 활동내역 가져오기 실패: 존재하지 않는 멤버")
    void test12_3() throws Exception {
        // given
        signup();
        login();
        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        articleService.createDummies(member);

        // expected
        mockMvc.perform(get("/members/{id}", 4321).cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}