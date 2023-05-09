package com.codein.controller;

import com.codein.config.SecurityConfig.MySecured;
import com.codein.crypto.PasswordEncoder;
import com.codein.domain.article.Category;
import com.codein.domain.member.Member;
import com.codein.domain.member.Role;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.article.GetArticlesDto;
import com.codein.requestdto.article.EditArticleDto;
import com.codein.requestdto.article.NewArticleDto;
import com.codein.responsedto.ArticleListResponseDto;
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
    private final MemberRepository memberRepository;
    private final ArticleService articleService;

    @GetMapping(value = {"/","/{category}"})
    public ArticleListResponseDto getMemberList(
            @PathVariable(value = "category", required = false) String category,
            @ModelAttribute GetArticlesDto getArticlesDto
          ) {

        if(memberRepository.findByEmail("kdha4585@gmail.com") == null){
            PasswordEncoder passwordEncoder = new PasswordEncoder();
            Member member = Member.builder()
                    .name("김동하")
                    .email("kdha4585@gmail.com")
                    .nickname("데일이")
                    .password(passwordEncoder.encrypt("12341234"))
                    .phone("01075444357")
                    .birth("1996-05-28")
                    .sex("male")
                    .build();
            member.setRole(Role.ADMIN);
            memberRepository.save(member);
        }   // 관리자 계정 없으면 생성

        if(category == null) {
            return articleService.getArticleList(getArticlesDto, Category.COMMUNITY);
        } else {
            return articleService.getArticleList(getArticlesDto, Category.valueOf(category.toUpperCase()));
        }
    }


    @MySecured(role = Role.ADMIN)
    @PostMapping( "/create-dummies")
    public void createDummies(@CookieValue(value = "accesstoken") Cookie cookie) {
        articleService.createDummies(memberRepository.findByAccessToken(cookie.getValue()));
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
