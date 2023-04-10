package com.codein.service;

import com.codein.domain.Session;
import com.codein.domain.article.Article;
import com.codein.domain.member.Member;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.SessionRepository;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.member.LoginDto;
import com.codein.requestdto.member.SignupDto;
import com.codein.requestdto.post.EditArticleDto;
import com.codein.requestdto.post.NewArticleDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ArticleServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private SessionRepository sessionRepository;
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

    String getToken() {
        Member member = memberRepository.findByEmail("kdha4585@gmail.com");
        Session session = sessionRepository.findByMember(member)
                .orElseThrow(MemberNotExistsException::new);
        return session.getAccessToken();
    }

    @Test
    @DisplayName("글 등록 성공")
    void test1() {
        // given
        String accessToken = getToken();
        NewArticleDto newArticleDto = NewArticleDto.builder()
                .category("NOTICE")
                .title("제목")
                .content("내용")
                .build();
        // when
        articleService.newArticle(newArticleDto.toWritePostServiceDto(), accessToken);

        //then
        Member member = memberRepository.findByAccessToken(accessToken);
        List<Article> articles = articleRepository.findByMember(member);
        Article article = articles.get(0);

        Assertions.assertEquals(newArticleDto.getCategory(), article.getCategory().getValue());
        Assertions.assertEquals(newArticleDto.getTitle(), article.getTitle());
        Assertions.assertEquals(newArticleDto.getContent(), article.getContent());
    }

    @Test
    @DisplayName("글 수정 성공")
    void test2() {
        // given
        String accessToken = getToken();

        NewArticleDto newArticleDto = NewArticleDto.builder()
                .category("NOTICE")
                .title("제목")
                .content("내용")
                .build();
        articleService.newArticle(newArticleDto.toWritePostServiceDto(), accessToken);

        Member member = memberRepository.findByAccessToken(accessToken);
        List<Article> articles = articleRepository.findByMember(member);
        Article article = articles.get(0);

        EditArticleDto editArticleDto = EditArticleDto.builder()
                .category("COMMUNITY")
                .title("타이틀")
                .content("내용")
                .build();
        // when
        articleService.editArticle(article.getId(), editArticleDto.toEditPostServiceDto());

        //then
        List<Article> editedArticles = articleRepository.findByMember(member);
        Article editedArticle = editedArticles.get(0);

        Assertions.assertEquals(editArticleDto.getCategory(), editedArticle.getCategory().getValue());
        Assertions.assertEquals(editArticleDto.getTitle(), editedArticle.getTitle());
        Assertions.assertEquals(editArticleDto.getContent(), editedArticle.getContent());
    }

}