package com.codein.service;
import com.codein.domain.article.*;
import com.codein.domain.member.Member;
import com.codein.error.exception.article.ArticleLikeExistsException;
import com.codein.error.exception.article.ArticleNotExistsException;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.error.exception.member.MemberNotLoginException;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.article.like.ArticleLikeRepository;
import com.codein.repository.article.viewlog.ViewLogRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.requestservicedto.article.*;
import com.codein.responsedto.article.ActivityListResponseDto;
import com.codein.responsedto.article.ArticleListResponseDto;
import com.codein.responsedto.article.GetArticleResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ViewLogRepository viewLogRepository;
    private final ArticleLikeRepository articleLikeRepository;

    @Transactional
    public Article newArticle(NewArticleServiceDto newArticleServiceDto, String accesstoken) {
        Member member = memberRepository.findByAccessToken(accesstoken);
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
        Article article = articleRepository.findById(getArticleServiceDto.getArticleId())
                .orElseThrow(ArticleNotExistsException::new);
        ViewLog viewLog = viewLogRepository.findByGetArticleServiceDto(getArticleServiceDto);

        boolean isNewView = false;

        if (viewLog == null || LocalDateTime.now().minus(10, ChronoUnit.MINUTES).isAfter(viewLog.getViewedAt())) {
            // view log가 없거나 10분 이상 지난 경우
            viewLogRepository.save(new ViewLog(article, getArticleServiceDto.getClientIp()));
            isNewView = true;
        }

        return article.toGetArticleResponseDto(isNewView);
    }


    @Transactional
    public ArticleListResponseDto getArticleList(GetArticlesServiceDto getArticlesServiceDto) {
        return articleRepository.getArticleList(getArticlesServiceDto);
    }

    @Transactional
    public void likeArticle(ArticleLikeServiceDto articleLikeServiceDto) {
        Article article = articleRepository.findById(articleLikeServiceDto.getArticleId())
                .orElseThrow(ArticleNotExistsException::new);
        boolean exists = articleLikeRepository.existsArticleLike(articleLikeServiceDto);

        if (!exists) {
            // 해당 article id와 client id로 like가 존재하지 않으면 like 생성
            article.increaseLikeNum();
            articleLikeRepository.save(ArticleLike.builder()
                    .article(article)
                    .member(article.getMember())
                    .build());

        } else {    // 존재할 경우 예외처리
            throw new ArticleLikeExistsException();
        }
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


}
