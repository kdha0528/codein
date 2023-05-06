package com.codein.controller;

import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.article.Category;
import com.codein.domain.member.Role;
import com.codein.requestdto.GetArticlesDto;
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

    @GetMapping(value = {"/","/{category}"})
    public List<ArticleListResponseDto> getMemberList(
            @RequestParam(required = false, defaultValue = "1") Integer page,
            @RequestParam(required = false, defaultValue = "all") String period,
            @RequestParam(required = false, defaultValue = "latest") String sort,
            @RequestParam(required = false) String condition,
            @RequestParam(required = false) String keyword,
            @PathVariable(value = "category", required = false) String category,
            @ModelAttribute GetArticlesDto getArticlesDto
          ) {

        System.out.println("page = "+ getArticlesDto.getPage());
        System.out.println("period = "+ getArticlesDto.getPeriod());
        System.out.println("latest = "+ getArticlesDto.getSort());

        if(category == null) {
            return articleService.getArticleList(getArticlesDto, Category.COMMUNITY);

        } else {
            return articleService.getArticleList(getArticlesDto, Category.valueOf(category.toUpperCase()));
        }
    }

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
}
