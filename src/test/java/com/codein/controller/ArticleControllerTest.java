package com.codein.controller;

import com.codein.domain.article.Article;
import com.codein.domain.article.Category;
import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.TokensRepository;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.article.viewlog.ViewLogRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.article.EditArticleDto;
import com.codein.requestdto.article.GetArticleDto;
import com.codein.requestdto.article.NewArticleDto;
import com.codein.requestdto.member.LoginDto;
import com.codein.requestdto.member.SignupDto;
import com.codein.requestservicedto.article.DeleteArticleServiceDto;
import com.codein.requestservicedto.article.GetArticleServiceDto;
import com.codein.service.ArticleService;
import com.codein.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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


    Article newArticle() {
        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        Article article = Article.builder()
                .member(member)
                .category(Category.COMMUNITY)
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        articleRepository.save(article);
        return article;
    }

    void createDummies(){
        Cookie cookie = getCookie();
        Random random = new Random();
        Member member = memberRepository.findByAccessToken(cookie.getValue());
        for(int i = 0; i < 100; i ++) {
            int categoryFlag = random.nextInt(4);
            Category category =  Category.COMMUNITY;
            switch(categoryFlag){
                case 1:
                    category = Category.QUESTION;
                    break;
                case 2:
                    category = Category.INFORMATION;
                default:
                    break;
            }

            Article article = Article.builder()
                    .member(member)
                    .category(category)
                    .title("Title No."+i)
                    .content("카테고리는 " + category.getName() + "입니다.")
                    .build();
            articleRepository.save(article);

        }
    }

    void createViewDummies(){
        Random random = new Random();
        List<Article> articleList = articleRepository.findAll();
        for (Article article : articleList) {
            for (int j = 0; j < random.nextInt(20); j++) {
                articleService.getArticle(GetArticleServiceDto.builder()
                                .articleId(article.getId())
                                .clientIp(UUID.randomUUID().toString())
                        .build());
            }
        }
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
        mockMvc.perform(post("/articles/{id}/edit",articleId).cookie(getCookie())
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
        mockMvc.perform(post("/articles/{id}/edit",articleId).cookie(getCookie())
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
        mockMvc.perform(post("/articles/{id}/edit",articleId).cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editArticleDto))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("글 목록 가져오기 성공")
    void test3_1() throws Exception {
        // given
        createDummies();
        String category = "community";
        // expected
        mockMvc.perform(get("/{category}", category).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 목록 가져오기 성공: 5페이지 가져오기")
    void test3_2() throws Exception {
        // given
        createDummies();
        String category = "community";

        // expected
        mockMvc.perform(get("/{category}?page=5&sort=LATEST&period=ALL",category).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 목록 가져오기 성공: 조회수로 정렬하기")
    void test3_3() throws Exception {
        // given
        createDummies();
        createViewDummies();
        String category = "community";

        // expected
        mockMvc.perform(get("/{category}?page=1&sort=VIEW&period=ALL",category).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 목록 가져오기 성공: 다른 카테고리")
    void test3_4() throws Exception {
        // given
        createDummies();
        String category = "information";

        // expected
        mockMvc.perform(get("/{category}?page=1&sort=LATEST&period=ALL",category).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 목록 가져오기 실패: 잘못된 path variable") //
    void test3_5() throws Exception {
        // given
        createDummies();
        String category = "community";

        // expected
        mockMvc.perform(get("/{category}?page=1&sort=abc&period=abc",category).cookie(getCookie()))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }
    @Test
    @DisplayName("글 목록 가져오기 성공: 존재하지 않는 페이지") // 아무것도 들어있지 않음
    void test3_6() throws Exception {
        // given
        createDummies();
        String category = "community";

        // expected
        mockMvc.perform(get("/{category}?page=12345&sort=LATEST&period=ALL",category).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("글 목록 가져오기 실패: 잘못된 카테고리") // category와 page를 제외한 path variable이 잘못된 경우 default 값으로 이동
    void test3_7() throws Exception {
        // given
        createDummies();
        String category = "abc";

        // expected
        mockMvc.perform(get("/{category}?page=1&sort=latest&period=all",category).cookie(getCookie()))
                .andExpect(status().is5xxServerError())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 가져오기 성공")
    void test4_1() throws Exception {
        // given
        Article article = newArticle();

        // expected
        mockMvc.perform(get("/articles/{id}",article.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 가져오기 실패: 존재하지 않는 id")
    void test4_2() throws Exception {
        // given
        Long id = 3213523521L;

        GetArticleDto getArticleDto = GetArticleDto.builder()
                .id(id)
                .build();

        // expected
        mockMvc.perform(get("/articles/{id}",id)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getArticleDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 가져오기 성공: id를 입력하지 않은 경우")
    void test4_3() throws Exception {

        Article article = newArticle();

        // expected
        mockMvc.perform(get("/articles/{id}",article.getId()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 가져오기 실패: 삭제된 게시글")
    void test4_4() throws Exception {
        Article article = newArticle();

        DeleteArticleServiceDto deleteArticleServiceDto = DeleteArticleServiceDto.builder()
                .accessToken(getCookie().getValue())
                .id(article.getId())
                .build();

        articleService.deleteArticle(deleteArticleServiceDto);

        GetArticleDto getArticleDto = GetArticleDto.builder()
                .id(article.getId())
                .build();

        // expected
        mockMvc.perform(get("/articles/{id}",article.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(getArticleDto)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 좋아요 누르기 성공")
    void test5_1() throws Exception {
        // given
        Article article = newArticle();
        Long id = article.getId();

        // expected
        mockMvc.perform(post("/articles/{id}/like",id).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 좋아요 누르기 실패: 중복 좋아요")
    void test5_2() throws Exception {
        // given
        Article article = newArticle();
        Long id = article.getId();
        mockMvc.perform(post("/articles/{id}/like",id).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());

        // expected
        mockMvc.perform(post("/articles/{id}/like",id).cookie(getCookie()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void test6_1() throws Exception {
        // given
        Article article = newArticle();
        Long id = article.getId();

        // expected
        mockMvc.perform(delete("/articles/{id}",id).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }
}