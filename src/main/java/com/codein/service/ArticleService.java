package com.codein.service;

import com.codein.domain.article.Article;
import com.codein.domain.article.ArticleEditor;
import com.codein.domain.article.Category;
import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import com.codein.error.exception.article.ArticleNotExistsException;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.error.exception.member.MemberNotLoginException;
import com.codein.repository.TokensRepository;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.repository.view.ArticleViewRepository;
import com.codein.requestservicedto.article.*;
import com.codein.responsedto.article.ActivityListResponseDto;
import com.codein.responsedto.article.ArticleListResponseDto;
import com.codein.responsedto.article.GetArticleResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TokensRepository tokensRepository;
    private final MemberRepository memberRepository;
    private final ArticleViewRepository articleViewRepository;

    @Transactional
    public Article newArticle(NewArticleServiceDto newArticleServiceDto, String accesstoken) {
        Tokens tokens = tokensRepository.findByAccessToken(accesstoken)
                .orElseThrow(MemberNotLoginException::new);
        Member member = tokens.getMember();
        Article article = newArticleServiceDto.toEntity(member);
        articleRepository.save(article);
        return article;
    }

    @Transactional
    public Article editArticle(EditArticleServiceDto editArticleServiceDto) {
        Article article = articleRepository.findById(editArticleServiceDto.getId())
                .orElseThrow(ArticleNotExistsException::new);

        ArticleEditor articleEditor = ArticleEditor.builder()
                .category(editArticleServiceDto.getCategory())
                .title(editArticleServiceDto.getTitle())
                .content(editArticleServiceDto.getContent())
                .build();

        return article.edit(articleEditor);
    }
    @Transactional
    public GetArticleResponseDto getArticle(GetArticleServiceDto getArticleServiceDto) {
        return getArticleServiceDto.toGetArticleResponseDto();
    }


    @Transactional
    public ArticleListResponseDto getArticleList(GetArticlesServiceDto getArticlesServiceDto) {
        return articleRepository.getArticleList(getArticlesServiceDto);
    }

    @Transactional
    public ActivityListResponseDto getActivityList(GetActivitiesServiceDto getActivitiesServiceDto) {
        Member member = memberRepository.findById(getActivitiesServiceDto.getId())
                .orElseThrow(MemberNotExistsException::new);
        return articleRepository.getActivityListResponseDto(getActivitiesServiceDto, member);
    }

    @Transactional
    public void createDummies(Member member) {
        if(member == null) throw new MemberNotLoginException();
        if(articleRepository.findByMember(member).isEmpty()){
            Random random = new Random();
            for(int i = 0; i < 600; i ++) {
                int categoryFlag = random.nextInt(3);
                Category category =  Category.COMMUNITY;
                switch(categoryFlag){
                    case 1:
                        category = Category.QUESTION;
                        break;
                    case 2:
                        category = Category.INFORMATION;
                    default:
                        break;
                }

                Article article = Article.builder()
                        .member(member)
                        .category(category)
                        .title("Title No."+i)
                        .content("카테고리는 " + category.getName() + "입니다.")
                        .build();
                articleRepository.save(article);
            }
        }
    }

    @Transactional
    public void addView(Article article, String clientIp) {
        articleViewRepository.addView(article, clientIp);
    }


}
