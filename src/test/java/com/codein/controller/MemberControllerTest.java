package com.codein.controller;

import com.codein.domain.Member;
import com.codein.domain.Role;
import com.codein.repository.MemberRepository;
import com.codein.requestdto.EditMemberDto;
import com.codein.requestdto.LoginDto;
import com.codein.requestdto.SignupDto;
import com.codein.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.AfterEach;
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
    private MemberService memberService;


    @AfterEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void test1_1() throws Exception {
        //given
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585@gmail.com") // email 형식
                .nickname("데일이") // 한글, 영어, 숫자 2~16
                .password("123a1A34")   // 8~20 숫자, 소문자, 대문자
                .name("데일이")    // 2~10글자
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
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
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
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
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
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
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
                .nickname("데일리")
                .password("12341234")
                .name("데일이")
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
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
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
                .nickname("데일이")
                .password("12341234@@")
                .name("데일이")
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
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
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
                .nickname("데일이")
                .password("1234123412351235123512351235123") // 20글자 까지
                .name("데일이")
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
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        mockMvc.perform(post("/signup")
                .content(objectMapper.writeValueAsString(signupDto))
                .contentType(APPLICATION_JSON));

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
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        mockMvc.perform(post("/signup")
                .content(objectMapper.writeValueAsString(signupDto))
                .contentType(APPLICATION_JSON));


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
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build(
                );
        mockMvc.perform(post("/signup")
                .content(objectMapper.writeValueAsString(signupDto))
                .contentType(APPLICATION_JSON));

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

        // expected
        mockMvc.perform(post("/logout").cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("redirect:/home"))
                .andDo(print());
    }

    @Test
    @DisplayName("로그아웃 실패 : 권한 없음")
    void test3_2() throws Exception {
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
                .contentType(APPLICATION_JSON)); // default로 role = member

        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();

        MvcResult result = mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andReturn();

        Cookie[] cookies = result.getResponse().getCookies();
        Member member = memberRepository.findByEmail("kdha4585@gmail.com");
        member.setRole(Role.NONE);   // 강제로 role 삭제
        memberRepository.save(member);

        // expected
        mockMvc.perform(post("/logout").cookie(cookies))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }

    @Test
    @DisplayName("회원정보 수정 성공")
    void test4_1() throws Exception {
        // given
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

        EditMemberDto editMemberDto = EditMemberDto.builder()
                .email("kdha0528@gmail.com")
                .nickname("데일이")
                .password("11112222")
                .name(null)
                .phone("01012341234")
                .build();

        // expected
        mockMvc.perform(post("/editmember").cookie(cookies)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editMemberDto))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원정보 수정 실패 : 이메일 형식 불일치")
    void test4_2() throws Exception {
        // given
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build();
        mockMvc.perform(post("/signup")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupDto)));

        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andReturn();

        Cookie[] cookies = loginResult.getResponse().getCookies();

        EditMemberDto editMemberDto = EditMemberDto.builder()
                .email("kdha4585")  // email 양식 틀림
                .nickname("데일이")
                .phone(null)
                .name(null)
                .password("12345678")
                .build();

        // expected
        mockMvc.perform(post("/editmember").cookie(cookies)
                        .content(objectMapper.writeValueAsString(editMemberDto))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("회원정보 수정 성공 : 전부 null인 경우")
    void test4_3() throws Exception {
        // given
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build();
        mockMvc.perform(post("/signup")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupDto)));

        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andReturn();

        Cookie[] cookies = loginResult.getResponse().getCookies();

        EditMemberDto editMemberDto = EditMemberDto.builder()    // 전부 null인 경우
                .email(null)
                .nickname(null)
                .password(null)
                .name(null)
                .phone(null)
                .build();

        // expected
        mockMvc.perform(post("/editmember").cookie(cookies)
                        .content(objectMapper.writeValueAsString(editMemberDto))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("회원정보 수정 성공 : 전부 그대로인 경우")
    void test4_4() throws Exception {
        // given
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build();
        mockMvc.perform(post("/signup")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupDto)));

        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andReturn();

        Cookie[] cookies = loginResult.getResponse().getCookies();

        EditMemberDto editMemberDto = EditMemberDto.builder()
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .build();

        // expected
        mockMvc.perform(post("/editmember").cookie(cookies)
                        .content(objectMapper.writeValueAsString(editMemberDto))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("회원탈퇴 성공")
    void test5_1() throws Exception {
        // given
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build();
        mockMvc.perform(post("/signup")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupDto)));

        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andReturn();

        Cookie[] cookies = loginResult.getResponse().getCookies();

        // expected
        mockMvc.perform(post("/deletemember").cookie(cookies))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("redirect:/home"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원탈퇴 실패 : 권한 없음")
    void test5_2() throws Exception {
        // given
        SignupDto signupDto = SignupDto.builder()
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .name("데일이")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build();
        mockMvc.perform(post("/signup")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupDto)));

        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        MvcResult loginResult = mockMvc.perform(post("/login")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andReturn();

        Cookie[] cookies = loginResult.getResponse().getCookies();
        Member member = memberRepository.findByEmail(signupDto.getEmail());
        member.setRole(Role.NONE);
        memberRepository.save(member);

        // expected
        mockMvc.perform(post("/deletemember").cookie(cookies))
                .andExpect(status().is4xxClientError())
                .andDo(print());
    }
}