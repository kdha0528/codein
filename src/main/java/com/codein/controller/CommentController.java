package com.codein.controller;

import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.member.Role;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.comment.EditCommentDto;
import com.codein.requestdto.comment.NewCommentDto;
import com.codein.requestservicedto.comment.CommentLikeServiceDto;
import com.codein.responsedto.comment.DeleteCommentServiceDto;
import com.codein.service.ArticleService;
import com.codein.service.CommentService;
import com.codein.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;



@Slf4j
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final MemberRepository memberRepository;
    private final MemberService memberService;
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final CommentService commentService;

    @MySecured(role = Role.MEMBER)
    @PostMapping( "/articles/{id}/comments")
    public void newComment(@CookieValue(value = "accesstoken") Cookie cookie,
                           @PathVariable(value = "id") Long id,
                           @Valid @RequestBody NewCommentDto newCommentDto) {
        commentService.newComment(newCommentDto.toNewCommentServiceDto(cookie.getValue(), id));
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping( "/articles/{id}/comments/{commentId}")
    public void editComment(@PathVariable(value = "id") Long id,
                            @PathVariable(value = "commentId") Long commentId,
                            @CookieValue(value = "accesstoken") Cookie cookie,
                            @RequestBody @Valid EditCommentDto editCommentDto){
        commentService.editComment(editCommentDto.toEditCommentServiceDto(cookie.getValue(),commentId));
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping( "/articles/{id}/comments/{commentId}/like")
    public void likeComment(@PathVariable(value = "id") Long id,
                            @PathVariable(value = "commentId") Long commentId,
                            @CookieValue(value = "accesstoken") Cookie cookie){
        CommentLikeServiceDto commentLikeServiceDto = CommentLikeServiceDto.builder()
                .commentId(commentId)
                .accessToken(cookie.getValue())
                .like(true)
                .build();
        commentService.likeComment(commentLikeServiceDto);
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping( "/articles/{id}/comments/{commentId}/dislike")
    public void dislikeComment(@PathVariable(value = "id") Long id,
                            @PathVariable(value = "commentId") Long commentId,
                            @CookieValue(value = "accesstoken") Cookie cookie){
        CommentLikeServiceDto commentLikeServiceDto = CommentLikeServiceDto.builder()
                                .commentId(commentId)
                                .accessToken(cookie.getValue())
                                .like(false)
                                .build();
        commentService.likeComment(commentLikeServiceDto);
    }

    @MySecured(role = Role.MEMBER)
    @DeleteMapping( "/articles/{id}/comments/{commentId}")
    public void deleteComment(@PathVariable(value = "id") Long id,
                               @PathVariable(value = "commentId") Long commentId,
                               @CookieValue(value = "accesstoken") Cookie cookie){
        DeleteCommentServiceDto deleteCommentServiceDto = DeleteCommentServiceDto.builder()
                .accessToken(cookie.getValue())
                .id(commentId)
                .build();
        commentService.deleteComment(deleteCommentServiceDto);
    }
}
