package com.codein.controller;

import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.article.Category;
import com.codein.domain.member.Role;
import com.codein.requestdto.PageSizeDto;
import com.codein.requestdto.article.EditArticleDto;
import com.codein.requestdto.article.NewArticleDto;
import com.codein.responsedto.ArticleListResponseDto;
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
    @PostMapping( "/article/new")
    public void newArticle(@CookieValue(value = "accesstoken") Cookie cookie, @RequestBody @Valid NewArticleDto newArticleDto) {
        articleService.newArticle(newArticleDto.toNewArticleServiceDto(), cookie.getValue());
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping("/article/edit")
    public void editArticle(@RequestBody @Valid EditArticleDto editArticleDto) {
        articleService.editArticle(editArticleDto.toEditArticleServiceDto());
    }

    @GetMapping(value = {"/","/{category}"})
    public List<ArticleListResponseDto> getMemberList(@ModelAttribute PageSizeDto pageSizeDto, @PathVariable("category") String category) {
        Category categoryParam = Category.COMMUNITY;

        if(category != null){
                categoryParam = Category.valueOf(category.toUpperCase());
        }

        if(pageSizeDto.getPage() == null) {
            return articleService.getArticleList(PageSizeDto.builder().build(), categoryParam);

        } else {
            return articleService.getArticleList(pageSizeDto, categoryParam);
        }

    }

}
