package com.codein.controller;

import com.codein.config.SecurityConfig;
import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.member.Role;
import com.codein.requestdto.PageSizeDto;
import com.codein.requestdto.article.EditArticleDto;
import com.codein.requestdto.article.NewArticleDto;
import com.codein.responsedto.ArticleListResponseDto;
import com.codein.responsedto.MemberListResponseDto;
import com.codein.service.ArticleService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @MySecured(role = Role.MEMBER)
    @PostMapping(value = "/article/new")
    public void newArticle(@CookieValue(value = "accesstoken") Cookie cookie, @RequestBody @Valid NewArticleDto newArticleDto) {
        articleService.newArticle(newArticleDto.toNewArticleServiceDto(), cookie.getValue());
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/article/edit")
    public void editArticle(@CookieValue(value = "accesstoken") Cookie cookie, @RequestBody @Valid EditArticleDto editArticleDto) {
        articleService.editArticle(editArticleDto.toEditArticleServiceDto());
    }

    @GetMapping(value = {"/","/articles"})
    public List<ArticleListResponseDto> getMemberList(@ModelAttribute PageSizeDto pageSizeDto) {
        return articleService.getArticleList(pageSizeDto);
    }

}
