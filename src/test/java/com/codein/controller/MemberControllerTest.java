package com.codein.controller;

import com.codein.domain.Member;
import com.codein.exception.NotSigninedAccount;
import com.codein.repository.MemberRepository;
import com.codein.request.Signin;
import com.codein.request.Signup;
import com.codein.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;


    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void test1() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("kdha4585@gmail.com")
                .password("1234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("남성")
                .build(
                );
        // expected
        mockMvc.perform(post("/signup")
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.
        System.out.println(" ");
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공")
    void test2() throws Exception {
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
        System.out.println(" ");

        // expected
        mockMvc.perform(post("/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그아웃 성공")
    void test3() throws Exception {
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
                .andExpect(MockMvcResultMatchers.content().string("redirect:/"))
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("권한 부여 후 로그아웃 실패")
    void test4() throws Exception {
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
                .andExpect(MockMvcResultMatchers.content().string("redirect:/"))
                .andDo(print());
    }
}