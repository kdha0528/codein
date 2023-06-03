package com.codein.service;

import com.codein.domain.article.Article;
import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import com.codein.error.exception.article.ArticleNotExistsException;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.TokensRepository;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.article.*;
import com.codein.requestdto.member.LoginDto;
import com.codein.requestdto.member.SignupDto;
import com.codein.requestservicedto.article.DeleteArticleServiceDto;
import com.codein.requestservicedto.article.GetArticleServiceDto;
import com.codein.responsedto.article.ActivityListResponseDto;
import com.codein.responsedto.article.ArticleListResponseDto;
import com.codein.responsedto.article.GetArticleResponseDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;


@SpringBootTest
class ArticleServiceTest {
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
                .name("김동하")
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
        Tokens tokens = tokensRepository.findByMember(member)
                .orElseThrow(MemberNotExistsException::new);
        return tokens.getAccessToken();
    }

    Article newArticle() {
        String accessToken = getToken();

        NewArticleDto newArticleDto = NewArticleDto.builder()
                .category("NOTICE")
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        return articleService.newArticle(newArticleDto.toNewArticleServiceDto(), accessToken);
    }

    void createDummies(){
        articleService.createDummies(memberRepository.findByAccessToken(getToken()));
        ArrayList<Member> memberList = memberService.createMemberDummies();
        articleService.createViewDummies();
        articleService.createLikeDummies(memberList);
    }

    @Test
    @DisplayName("글 등록 성공")
        void test1() {
        // given
        String accessToken = getToken();
        NewArticleDto newArticleDto = NewArticleDto.builder()
                .category("NOTICE")
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        // when
        Article article = articleService.newArticle(newArticleDto.toNewArticleServiceDto(), accessToken);

        //then

        Assertions.assertEquals(newArticleDto.getCategory(), article.getCategory().getName());
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
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        Article article = articleService.newArticle(newArticleDto.toNewArticleServiceDto(), accessToken);

        EditArticleDto editArticleDto = EditArticleDto.builder()
                .category("COMMUNITY")
                .title("타이틀")
                .content("내용")
                .build();

        // when
        Article editedArticle = articleService.editArticle(editArticleDto.toEditArticleServiceDto(article.getId(), getToken()));

        //then

        Assertions.assertEquals(editArticleDto.getCategory(), editedArticle.getCategory().getName());
        Assertions.assertEquals(editArticleDto.getTitle(), editedArticle.getTitle());
        Assertions.assertEquals(editArticleDto.getContent(), editedArticle.getContent());
    }

    @Test
    @DisplayName("글 목록 가져오기 성공")
    void test3() {
        // given
        createDummies();
        GetArticlesDto getArticlesDto = GetArticlesDto.builder()
                .build();

        // when
        ArticleListResponseDto articleList = articleService.getArticleList(getArticlesDto.toGetArticlesServiceDto());

        //then
        Assertions.assertEquals(20L, articleList.getArticleList().size());
    }

    @Test
    @DisplayName("멤버 활동 내역 가져오기")
    void test4() {
        // given
        createDummies();
        Member member = memberRepository.findByAccessToken(getToken());
        GetActivitiesDto getActivitiesDto = GetActivitiesDto.builder()
                .id(member.getId())
                .build();

        // when
        ActivityListResponseDto activityList = articleService.getActivityList(getActivitiesDto.toGetActivitiesServiceDto());

        //then
        Assertions.assertEquals(20L,activityList.getActivityList().size()) ;
    }


    @Test
    @DisplayName("게시글 가져오기")
    void test5() {
        // given
        Article article = newArticle();

        // when
        GetArticleResponseDto getArticleResponseDto = articleService.getArticle(GetArticleServiceDto.builder()
                        .articleId(article.getId())
                        .clientIp("client ip")
                .build());

        //then
        Assertions.assertEquals(getArticleResponseDto.getArticleData().getCategory(), article.getCategory().getName());
        Assertions.assertEquals(getArticleResponseDto.getArticleData().getTitle(), article.getTitle());
        Assertions.assertEquals(getArticleResponseDto.getArticleData().getContent(), article.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test6() {
        // given
        Article article = newArticle();

        // when
        DeleteArticleServiceDto deleteArticleServiceDto = DeleteArticleServiceDto.builder()
                .id(article.getId())
                .accessToken(getToken())
                .build();
        articleService.deleteArticle(deleteArticleServiceDto);

        //then
        Article deletedArticle = articleRepository.findById(article.getId())
                .orElseThrow(ArticleNotExistsException::new);

        Assertions.assertTrue(deletedArticle.isDeleted());
    }
}