package com.codein.controller;

import com.codein.requestdto.article.EditArticleDto;
import com.codein.requestdto.article.NewArticleDto;
import com.codein.service.ArticleService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping(value = {"/{category}/new", "/articles/new"})
    public void newArticle(@RequestBody @Valid NewArticleDto newArticleDto, @CookieValue(value = "accesstoken") Cookie cookie) {
        articleService.newArticle(newArticleDto.toNewArticleServiceDto(), cookie.getValue());
    }

    @PostMapping("/articles/{articleId}/edit")
    public void editArticle(@PathVariable Long articleId, @RequestBody @Valid EditArticleDto editArticleDto) {
        articleService.editArticle(articleId, editArticleDto.toEditArticleServiceDto());
    }

}
