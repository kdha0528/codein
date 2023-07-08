package com.codein.service;

import com.codein.domain.article.Article;
import com.codein.domain.auth.Tokens;
import com.codein.domain.comment.Comment;
import com.codein.domain.member.Member;
import com.codein.error.exception.comment.CommentNotExistsException;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.tokens.TokensRepository;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.comment.CommentRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.article.NewArticleDto;
import com.codein.requestdto.comment.EditCommentDto;
import com.codein.requestdto.comment.GetCommentListServiceDto;
import com.codein.requestdto.comment.NewCommentDto;
import com.codein.requestdto.member.LoginDto;
import com.codein.requestdto.member.SignupDto;
import com.codein.responsedto.comment.CommentListResponseDto;
import com.codein.responsedto.comment.DeleteCommentServiceDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommentServiceTest {

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
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        memberRepository.deleteAll();
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

    Comment newComment(Article article){
        NewCommentDto newCommentDto = NewCommentDto.builder()
                .content("댓글입니다.")
                .build();
        return commentService.newComment(newCommentDto.toNewCommentServiceDto(getToken(),article.getId()));
    }
    @Test
    @DisplayName("댓글 등록 성공")
    void test1() {
        // given
        String accessToken = getToken();
        Article article = newArticle();
        NewCommentDto newCommentDto = NewCommentDto.builder()
                .content("댓글입니다.")
                .build();

        // when
        Comment comment = commentService.newComment(newCommentDto.toNewCommentServiceDto(accessToken,article.getId()));

        // then
        Member member = memberRepository.findByAccessToken(accessToken);

        Assertions.assertEquals(newCommentDto.getContent(), comment.getContent());
        Assertions.assertEquals(member.getId(), comment.getMember().getId());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void test2() {
        // given
        String accessToken = getToken();
        Article article = newArticle();
        Comment comment = newComment(article);

        EditCommentDto editCommentDto = EditCommentDto.builder()
                .content("댓글인데용.")
                .build();

        // when
        Comment editedComment = commentService.editComment(editCommentDto.toEditCommentServiceDto(accessToken, comment.getId()));

        // then
        Assertions.assertEquals(editCommentDto.getContent(), editedComment.getContent());
    }

    @Test
    @DisplayName("댓글 목록 가져오기")
    void test3() {
        // given
        String accessToken = getToken();
        Article article = newArticle();
        Member member = memberRepository.findByAccessToken(accessToken);
        commentService.createCommentDummies(article,member);

        // when
        CommentListResponseDto commentListResponseDto = commentService.getCommentList(GetCommentListServiceDto.builder()
                        .articleId(article.getId())
                        .build());

        // then
        Assertions.assertEquals(commentListResponseDto.getCommentList().size(), 50);
    }

    @Test
    @DisplayName("댓글 삭제")
    void test4(){
        // given
        String accessToken = getToken();
        Article article = newArticle();
        Comment comment = newComment(article);

        DeleteCommentServiceDto deleteCommentServiceDto = DeleteCommentServiceDto.builder()
                .id(comment.getId())
                .accessToken(accessToken)
                .build();
        // when
        commentService.deleteComment(deleteCommentServiceDto);

        // then
        Comment deletedComment = commentRepository.findById(comment.getId())
                .orElseThrow(CommentNotExistsException::new);
        Assertions.assertTrue(deletedComment.isDeleted());
    }

}
