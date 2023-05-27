package com.codein.controller;

import com.codein.domain.article.Article;
import com.codein.domain.auth.Tokens;
import com.codein.domain.comment.Comment;
import com.codein.domain.member.Member;
import com.codein.error.exception.comment.CommentNotExistsException;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.TokensRepository;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.comment.CommentRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.article.NewArticleDto;
import com.codein.requestdto.comment.EditCommentDto;
import com.codein.requestdto.comment.NewCommentDto;
import com.codein.requestdto.member.LoginDto;
import com.codein.requestdto.member.SignupDto;
import com.codein.service.ArticleService;
import com.codein.service.CommentService;
import com.codein.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerTest {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;

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
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        memberRepository.deleteAll();
    }

    Cookie getCookie() {
        Member member = memberRepository.findByEmail("kdha4585@gmail.com");
        Tokens tokens = tokensRepository.findByMember(member)
                .orElseThrow(MemberNotExistsException::new);
        String token = tokens.getAccessToken();

        return new Cookie("accesstoken", token);
    }

    Article newArticle() {
        NewArticleDto newArticleDto = NewArticleDto.builder()
                .category("NOTICE")
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        return articleService.newArticle(newArticleDto.toNewArticleServiceDto(), getCookie().getValue());
    }

    Comment newComment(Article article){
        NewCommentDto newCommentDto = NewCommentDto.builder()
                .content("댓글입니다.")
                .build();
        return commentService.newComment(newCommentDto.toNewCommentServiceDto(getCookie().getValue(),article.getId()));
    }

    @Test
    @DisplayName("댓글 등록 성공")
    void test1_1() throws Exception {
        //given
        Article article = newArticle();
        Long id = article.getId();

        NewCommentDto newCommentDto = NewCommentDto.builder()
                .content("댓글입니다.")
                .build();

        // expected
        mockMvc.perform(post("/articles/{id}/comments",id).cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCommentDto))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("댓글 등록 성공: target 있음")
    void test1_2() throws Exception {
        //given
        Article article = newArticle();
        Long id = article.getId();
        Comment comment = newComment(article);

        NewCommentDto childCommentDto = NewCommentDto.builder()
                .targetId(comment.getId())
                .content("대댓글입니다.")
                .build();

        // expected
        mockMvc.perform(post("/articles/{id}/comments",id).cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(childCommentDto))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 등록 실패: 내용 없음")
    void test1_3() throws Exception {
        //given
        Article article = newArticle();
        Long id = article.getId();

        NewCommentDto commentDto = NewCommentDto.builder()
                .content("")
                .build();

        // expected
        mockMvc.perform(post("/articles/{id}/comments",id).cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto))
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 등록 수정 성공")
    void test2_1() throws Exception {
        //given
        Article article = newArticle();
        Comment comment = newComment(article);

        EditCommentDto editCommentDto = EditCommentDto.builder()
                .content("댓글인데용")
                .build();

        // expected
        mockMvc.perform(post("/articles/{id}/comments/{commentId}",article.getId(), comment.getId()).cookie(getCookie())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editCommentDto))
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 추천 누르기 성공")
    void test3_1() throws Exception {
        //given
        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        Article article = newArticle();
        Comment comment = newComment(article);
        
        // expected
        mockMvc.perform(post("/articles/{id}/comments/{commentId}/like",article.getId(), comment.getId()).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("댓글 비추천 누르기 성공")
    void test3_2() throws Exception {
        //given
        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        Article article = newArticle();
        Comment comment = newComment(article);

        // expected
        mockMvc.perform(post("/articles/{id}/comments/{commentId}/dislike",article.getId(), comment.getId()).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("댓글 추천 누르기 실패: 5초 이내 같은 계정으로 같은 댓글 추천")
    void test3_3() throws Exception {
        //given
        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        Article article = newArticle();
        Comment comment = newComment(article);

        mockMvc.perform(post("/articles/{id}/comments/{commentId}/dislike",article.getId(), comment.getId()).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());

        // expected
        mockMvc.perform(post("/articles/{id}/comments/{commentId}/like",article.getId(), comment.getId()).cookie(getCookie()))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("댓글 추천 누르기 성공: 비추천 후 추천")
    void test3_4() throws Exception {
        //given
        Member member = memberRepository.findByAccessToken(getCookie().getValue());
        Article article = newArticle();
        Comment comment = newComment(article);

        mockMvc.perform(post("/articles/{id}/comments/{commentId}/dislike",article.getId(), comment.getId()).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());

        Comment dislikedComment = commentRepository.findById(comment.getId())
                .orElseThrow(CommentNotExistsException::new);
        Assertions.assertEquals(0, (int) dislikedComment.getLikeNum());
        Assertions.assertEquals(1, (int) dislikedComment.getDislikeNum());

        Thread.sleep(10000);

        // expected
        mockMvc.perform(post("/articles/{id}/comments/{commentId}/like",article.getId(), comment.getId()).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());

        Comment likedComment = commentRepository.findById(comment.getId())
                        .orElseThrow(CommentNotExistsException::new);
        Assertions.assertEquals(1, (int) likedComment.getLikeNum());
        Assertions.assertEquals(0, (int) likedComment.getDislikeNum());
    }

    @Test
    @DisplayName("댓글 추천 누르기 성공: 추천 후 추천")
    void test3_5() throws Exception {
        //given
        Article article = newArticle();
        Comment comment = newComment(article);

        mockMvc.perform(post("/articles/{id}/comments/{commentId}/like",article.getId(), comment.getId()).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());

        Comment likedComment = commentRepository.findById(comment.getId())
                .orElseThrow(CommentNotExistsException::new);
        Assertions.assertEquals(1, (int) likedComment.getLikeNum());
        Assertions.assertEquals(0, (int) likedComment.getDislikeNum());

        Thread.sleep(10000);

        // expected
        mockMvc.perform(post("/articles/{id}/comments/{commentId}/like",article.getId(), comment.getId()).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());

        Comment unlikedComment = commentRepository.findById(comment.getId())
                .orElseThrow(CommentNotExistsException::new);
        Assertions.assertEquals(0, (int) unlikedComment.getLikeNum());
        Assertions.assertEquals(0, (int) unlikedComment.getDislikeNum());
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void test4_1() throws Exception {
        // given
        Article article = newArticle();
        Comment comment = newComment(article);

        // expected
        mockMvc.perform(delete("/articles/{id}/comments/{commentId}",article.getId(), comment.getId()).cookie(getCookie()))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
