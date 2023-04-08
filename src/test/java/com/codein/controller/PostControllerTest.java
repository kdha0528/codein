package com.codein.controller;

import com.codein.domain.Session;
import com.codein.domain.member.Member;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.SessionRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.repository.post.PostRepository;
import com.codein.requestdto.member.LoginDto;
import com.codein.requestdto.member.SignupDto;
import com.codein.requestdto.post.WritePostDto;
import com.codein.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
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
class PostControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void signupLogin() {
        SignupDto signupDto = SignupDto.builder()
                .name("데일이")
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build();
        memberService.signup(signupDto.toSignupServiceDto());

        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        memberService.login(loginDto.toMemberServiceDto());
    }

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
    }

    Cookie getCookie() {
        Member member = memberRepository.findByEmail("kdha4585@gmail.com");
        Session session = sessionRepository.findByMember(member)
                .orElseThrow(MemberNotExistsException::new);
        String token = session.getAccessToken();

        return new Cookie("accesstoken", token);
    }

    @Test
    @DisplayName("글 등록 성공")
    void test1_1() throws Exception {
        //given
        WritePostDto writePostDto = WritePostDto.builder()
                .category("NOTICE")
                .title("제목")
                .content("내용")
                .build();

        // expected
        mockMvc.perform(post("/writepost").cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writePostDto))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 등록 실패: 제목 없음")
    void test1_2() throws Exception {
        //given
        WritePostDto writePostDto = WritePostDto.builder()
                .category("NOTICE")
                .title("")
                .content("내용")
                .build();

        // expected
        mockMvc.perform(post("/writepost").cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writePostDto))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("글 등록 실패: 내용 없음")
    void test1_3() throws Exception {
        //given
        WritePostDto writePostDto = WritePostDto.builder()
                .category("NOTICE")
                .title("제목")
                .content("")
                .build();

        // expected
        mockMvc.perform(post("/writepost").cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writePostDto))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}