package com.codein.controller;

import com.codein.domain.Member;
import com.codein.domain.Session;
import com.codein.exception.AlreadyExistsAccountException;
import com.codein.exception.NotExistsAccountException;
import com.codein.exception.NotSigninedAccount;
import com.codein.repository.MemberRepository;
import com.codein.repository.SessionRepository;
import com.codein.request.MemberEdit;
import com.codein.request.Signin;
import com.codein.request.Signup;
import com.codein.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MemberControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private MemberService memberService;


    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void test1_1() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("kdha4585@gmail.com") // email 형식
                .password("123a1A34")   // 8~20 숫자, 소문자, 대문자
                .name("데일이")    // 2~10글자
                .phone("01075444357")   // 전화번호 형식 '-'제외
                .birth("1996-05-28")    // 날짜 형식
                .sex("male")    // male or female
                .build();
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("회원가입 실패 : 중복된 이메일")
    void test1_2() throws Exception {
        //given
        Signup signup1 = Signup.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        memberService.signup(signup1);

        Signup signup2 = Signup.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .name("데일이")
                .phone("01044444444")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );

        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signup2))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(new AlreadyExistsAccountException().getMessage()))
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("회원가입 실패 : 중복된 전화번호")
    void test1_3() throws Exception {
        //given
        Signup signup1 = Signup.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        memberService.signup(signup1);

        Signup signup2 = Signup.builder()
                .email("kdha1234@gmail.com")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signup2))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("회원가입 실패 : 올바르지 않은 형식의 전화번호")
    void test1_4() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("kdha1234@gmail.com")
                .password("12341234")
                .name("데일이")
                .phone("010-7544-4357") // "-" 없어야됨
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("회원가입 실패 : 비밀번호 특수문자 사용")
    void test1_5() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("kdha4585@gmail.com")
                .password("12341234@@")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("회원가입 실패 : 이메일 형식 불일치")
    void test1_6() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("kdha4585")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("회원가입 실패 : 비밀번호 글자 수 초과")
    void test1_7() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("kdha4585@gmail.com")
                .password("1234123412351235123512351235123") // 20글자 까지
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
    }

    @Test
    @DisplayName("로그인 성공")
    void test2_1() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        memberService.signup(signup);

        Signin signin = Signin.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();

        // expected
        mockMvc.perform(post("/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 : 비밀번호 불일치")
    void test2_2() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        memberService.signup(signup);

        Signin signin = Signin.builder()
                .email("kdha4585@gmail.com")
                .password("12345678")
                .build();

        // expected
        mockMvc.perform(post("/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 실패 : 존재하지 않는 이메일")
    void test2_3() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        memberService.signup(signup);

        Signin signin = Signin.builder()
                .email("kdha0528@gmail.com")
                .password("12341234")
                .build();

        // expected
        mockMvc.perform(post("/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("로그아웃 성공")
    void test3_1() throws Exception {
        //given
        Signup signup = Signup.builder()
                .name("데일이")
                .email("kdha4585@gmail.com")
                .password("1234")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("남성")
                .build();
        memberService.signup(signup);

        Signin signin = Signin.builder()
                .email("kdha4585@gmail.com")
                .password("1234")
                .build();

        MvcResult result = mockMvc.perform(post("/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        // expected
        mockMvc.perform(post("/logout").cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("redirect:/home"))
                .andDo(print());
    }

    @Test
    @DisplayName("권한 소멸 후 로그아웃 실패")
    void test3_2() throws Exception {
        //given
        Signup signup = Signup.builder()
                .name("데일이")
                .email("kdha4585@gmail.com")
                .password("1234")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("남성")
                .build();
        memberService.signup(signup); // default로 role = member

        Signin signin = Signin.builder()
                .email("kdha4585@gmail.com")
                .password("1234")
                .build();

        MvcResult result = mockMvc.perform(post("/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();
        Member member = memberRepository.findByEmail("kdha4585@gmail.com")
                .orElseThrow(NotSigninedAccount::new);
        member.setRole(null);

        // expected
        mockMvc.perform(post("/logout").cookie(cookies))
                .andExpect(MockMvcResultMatchers.content().string("")) // 권한 실패로 logout 컨트롤러를 아예 지나지 않음
                .andDo(print());
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