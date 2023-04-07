package com.codein.controller;

import com.codein.repository.member.MemberRepository;
import com.codein.repository.post.PostRepository;
import com.codein.requestdto.LoginDto;
import com.codein.requestdto.PostingDto;
import com.codein.requestdto.SignupDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 등록: 성공")
    void test1_1() throws Exception {
        //given
        SignupDto signupDto = SignupDto.builder()
                .name("데일이")
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build();
        mockMvc.perform(post("/signup")
                .content(objectMapper.writeValueAsString(signupDto))
                .contentType(APPLICATION_JSON));

        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();

        PostingDto postingDto = PostingDto.builder()
                .category("NOTICE")
                .title("제목")
                .content("내용")
                .build();

        // expected
        mockMvc.perform(post("/writepost").cookie(cookies)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postingDto))
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

}