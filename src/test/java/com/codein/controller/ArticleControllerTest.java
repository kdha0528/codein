package com.codein.controller;

import com.codein.domain.article.Article;
import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.TokensRepository;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.article.EditArticleDto;
import com.codein.requestdto.article.NewArticleDto;
import com.codein.requestdto.member.LoginDto;
import com.codein.requestdto.member.SignupDto;
import com.codein.service.ArticleService;
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

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ArticleService articleService;

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
        articleRepository.deleteAll();
    }

    Cookie getCookie() {
        Member member = memberRepository.findByEmail("kdha4585@gmail.com");
        Tokens tokens = tokensRepository.findByMember(member)
                .orElseThrow(MemberNotExistsException::new);
        String token = tokens.getAccessToken();

        return new Cookie("accesstoken", token);
    }

    void newArticle() {
        NewArticleDto newArticleDto = NewArticleDto.builder()
                .category("NOTICE")
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        articleService.newArticle(newArticleDto.toNewArticleServiceDto(), getCookie().getValue());
    }

    @Test
    @DisplayName("글 등록 성공")
    void test1_1() throws Exception {
        //given
        NewArticleDto newArticleDto = NewArticleDto.builder()
                .category("NOTICE")
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        // expected
        mockMvc.perform(post("/articles/new").cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newArticleDto))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 등록 실패: 제목 없음")
    void test1_2() throws Exception {
        //given
        NewArticleDto newArticleDto = NewArticleDto.builder()
                .category("NOTICE")
                .title("")
                .content("내용입니다.")
                .build();

        // expected
        mockMvc.perform(post("/articles/new").cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newArticleDto))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("글 등록 실패: 내용 없음")
    void test1_3() throws Exception {
        //given
        NewArticleDto writePostDto = NewArticleDto.builder()
                .category("NOTICE")
                .title("제목입니다.")
                .content("")
                .build();

        // expected
        mockMvc.perform(post("/articles/new").cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(writePostDto))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 성공")
    void test2_1() throws Exception {
        //given
        newArticle();

        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        List<Article> articles = articleRepository.findByMember(member);
        Article article = articles.get(0);
        Long articleId = article.getId();

        EditArticleDto editArticleDto = EditArticleDto.builder()
                .category("COMMUNITY")
                .title("타이틀입니다.")
                .content("내용입니다.")
                .build();

        // expected
        mockMvc.perform(post("/articles/{articleId}/edit", articleId).cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editArticleDto))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 실패: 제목 글자 수 미달")
    void test2_2() throws Exception {
        //given
        newArticle();

        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        List<Article> articles = articleRepository.findByMember(member);
        Article article = articles.get(0);
        Long articleId = article.getId();

        EditArticleDto editArticleDto = EditArticleDto.builder()
                .category("COMMUNITY")
                .title("타이틀")
                .content("내용입니다.")
                .build();

        // expected
        mockMvc.perform(post("/articles/{articleId}/edit", articleId).cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editArticleDto))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("글 수정 실패: 내용 글자 수 미달")
    void test2_3() throws Exception {
        //given
        newArticle();

        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        List<Article> articles = articleRepository.findByMember(member);
        Article article = articles.get(0);
        Long articleId = article.getId();

        EditArticleDto editArticleDto = EditArticleDto.builder()
                .category("COMMUNITY")
                .title("타이틀입니다.")
                .content("내용")
                .build();

        // expected
        mockMvc.perform(post("/articles/{articleId}/edit", articleId).cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editArticleDto))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}