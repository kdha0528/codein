package com.codein.controller;

import com.codein.domain.Member;
import com.codein.repository.MemberRepository;
import com.codein.request.Signin;
import com.codein.request.Signup;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

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
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공")
    void test2() throws Exception {
        //given
        Member member = Member.builder()
                .name("데일이")
                .email("kdha4585@gmail.com")
                .password("1234")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("남성")
                .build();

        memberRepository.save(member);

        Signin signin = Signin.builder() //테스트 할 때는 객체 클래스에 생성자 빌더 어노테이션 달아서 생성
                .email("kdha4585@gmail.com")
                .password("1234")
                .build();

        // expected
        mockMvc.perform(post("/signin")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signin)))
                .andExpect(status().isOk())
                .andDo(print()); // Http 요청에 대한 summary 를 볼 수 있음.

    }
}