package com.codein.controller;

import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.member.Role;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.comment.GetCommentListServiceDto;
import com.codein.requestdto.comment.NewCommentDto;
import com.codein.responsedto.comment.CommentListItem;
import com.codein.service.ArticleService;
import com.codein.service.CommentService;
import com.codein.service.MemberService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


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
    @PostMapping( "/articles/{id}/new")
    public void newComment(@CookieValue(value = "accesstoken") Cookie cookie,
                           @PathVariable(value = "id") Long id,
                           @RequestBody NewCommentDto newCommentDto) {
        commentService.newComment(newCommentDto.toNewCommentServiceDto(cookie.getValue(), id));
    }

}
