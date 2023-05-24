package com.codein.service;
import com.codein.domain.article.*;
import com.codein.domain.member.Member;
import com.codein.error.exception.article.*;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

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
        Article latestArticle = articleRepository.findByMemberLatest(member);

        // 한 유저는 1분에 1번씩만 게시글을 작성할 수 있습니다.
        if(latestArticle == null || LocalDateTime.now().minus(1, ChronoUnit.MINUTES).isAfter(latestArticle.getCreatedAt())){
            Article article = newArticleServiceDto.toEntity(member);
            articleRepository.save(article);
            return article;
        } else {
            throw new FrequentWriteException();
        }
    }

    @Transactional
    public Article editArticle(EditArticleServiceDto editArticleServiceDto) {
        Article article = articleRepository.findById(editArticleServiceDto.getId())
                .orElseThrow(ArticleNotExistsException::new);
        Member member = memberRepository.findByAccessToken(editArticleServiceDto.getAccessToken());
        if(article.getMember() == member) {

            ArticleEditor articleEditor = ArticleEditor.builder()
                    .category(editArticleServiceDto.getCategory())
                    .title(editArticleServiceDto.getTitle())
                    .content(editArticleServiceDto.getContent())
                    .build();

            return article.edit(articleEditor);
        }else{
            throw new InvalidAuthorException();
        }
    }
    @Transactional
    public GetArticleResponseDto getArticle(GetArticleServiceDto getArticleServiceDto) {
        Article article = articleRepository.findById(getArticleServiceDto.getArticleId())
                .orElseThrow(ArticleNotExistsException::new);

        boolean isNewView = false;

        if(!article.isDeleted()) {
            ViewLog viewLog = viewLogRepository.findByGetArticleServiceDto(getArticleServiceDto);

            if (viewLog == null || LocalDateTime.now().minus(10, ChronoUnit.MINUTES).isAfter(viewLog.getViewedAt())) {
                // view log가 없거나 10분 이상 지난 경우
                viewLogRepository.save(new ViewLog(article, getArticleServiceDto.getClientIp()));
                isNewView = true;
            }
        } else {
            throw new DeletedArticleException();
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

        Member member = memberRepository.findByAccessToken(articleLikeServiceDto.getAccessToken());

        if(member != null) {
            boolean exists = articleLikeRepository.existsArticleLike(article, member);

            if (!exists) {
                // 해당 article id와 client id로 like가 존재하지 않으면 like 생성
                article.increaseLikeNum();
                ArticleLike articleLike = ArticleLike.builder()
                        .article(article)
                        .member(member)
                        .build();
                articleLikeRepository.save(articleLike);
            } else {    // 존재할 경우 예외처리
                throw new ArticleLikeExistsException();
            }
        } else {
            throw new MemberNotExistsException();
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

    @Transactional
    public void createViewDummies(){
        Random random = new Random();
        List<Article> articleList = articleRepository.findAll();
        for (Article article : articleList) {
            for (int j = 0; j < random.nextInt(20); j++) {
                getArticle(new GetArticleServiceDto(article.getId(), UUID.randomUUID().toString()));
            }
        }
    }
    @Transactional
    public void createLikeDummies(ArrayList<Member> memberList){
        Random random = new Random();
        List<Article> articleList = articleRepository.findAll();
        boolean exists;
        for (Article article : articleList) {
            for (int j = 0; j < random.nextInt(20); j++) {

                exists = articleLikeRepository.existsArticleLike(article, memberList.get(j));

                if (!exists) {
                    article.increaseLikeNum();
                    ArticleLike articleLike = ArticleLike.builder()
                            .article(article)
                            .member(memberList.get(j))
                            .build();
                    articleLikeRepository.save(articleLike);
                } else {
                    throw new ArticleLikeExistsException();
                }
            }
        }
    }
    @Transactional
    public void deleteArticle(DeleteArticleServiceDto deleteArticleServiceDto){
        Article article = articleRepository.findById(deleteArticleServiceDto.getArticleId())
                .orElseThrow(ArticleNotExistsException::new);

        Member member = memberRepository.findByAccessToken(deleteArticleServiceDto.getAccessToken());

        if(article.getMember() == member){
            article.deleteArticle();
        } else if (member == null){
            throw new MemberNotExistsException();
        } else{
            throw new InvalidAuthorException();
        }
    }

}
