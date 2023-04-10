package com.codein.controller;

import com.codein.requestdto.post.NewArticleDto;
import com.codein.service.ArticleService;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping(value = {"/{category}/new", "/articles/new"})
    public String newArticle(@RequestBody @Valid NewArticleDto newArticleDto, @CookieValue(value = "accesstoken") Cookie cookie) {
        articleService.newArticle(newArticleDto.toWritePostServiceDto(), cookie.getValue());
        return "redirect:/home";
    }


}
