package com.codein.repository.article;

import com.codein.domain.article.Article;
import com.codein.domain.member.Member;
import com.codein.requestdto.article.GetActivityDto;
import com.codein.requestdto.article.GetArticlesDto;
import com.codein.responsedto.ActivityListItem;
import com.codein.responsedto.ActivityListResponseDto;
import com.codein.responsedto.ArticleListItem;
import com.codein.responsedto.ArticleListResponseDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.codein.domain.article.QArticle.article;
import static com.codein.domain.article.QLike.like;
import static com.codein.domain.comment.QComment.comment;


@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Article> findByMember(Member member) {
        return jpaQueryFactory.selectFrom(article)
                .where(article.member.eq(member))
                .fetch();
    }

    @Override
    public ArticleListResponseDto getArticleList(GetArticlesDto getArticlesDto) {

        JPAQuery<Article> query = jpaQueryFactory.selectFrom(article)
                .where(
                        article.category.eq(getArticlesDto.getCategory()),
                        article.createdAt.between(getArticlesDto.getStartDate(),LocalDateTime.now())
                );

        long count = query.fetch().size();
        int maxPage = (int) Math.floorDiv(count, getArticlesDto.getSize());

        if (getArticlesDto.getKeyword() != null) {
            switch(getArticlesDto.getCondition()) {
                case TITLE -> query.where(article.title.contains(getArticlesDto.getKeyword()));
                case AUTHOR -> query.where(article.member.nickname.contains(getArticlesDto.getKeyword()));
                default -> query.where(article.title.contains(getArticlesDto.getKeyword()).or(article.content.contains(getArticlesDto.getKeyword())));
            }
        }

        switch (getArticlesDto.getSort()) {
            case VIEW -> query.orderBy(article.viewNum.desc(), article.id.desc());
            case LIKE -> query.orderBy(article.likeNum.desc(), article.id.desc());
            default -> query.orderBy(article.id.desc());
        }

        List<Article> fetchResult = query
                .limit(getArticlesDto.getSize())
                .offset(getArticlesDto.getOffset())
                .fetch();

        List<ArticleListItem> articleList = fetchResult
                .stream()
                .map(Article::toArticleListItem)
                .collect(Collectors.toList());

        return ArticleListResponseDto.builder()
                .articleList(articleList)
                .maxPage(maxPage)
                .build();
    }

    @Override
    public ActivityListResponseDto getActivityListResponseDto(GetActivityDto getActivityDto, Member member) {

        JPAQuery<Article> query = switch (getActivityDto.getActivity()) {
            case COMMENTS -> jpaQueryFactory.select(comment.article)
                    .innerJoin(comment)
                    .where(comment.commenter.id.eq(getActivityDto.getId()));
            case LIKED_ARTICLES -> jpaQueryFactory.select(like.article)
                    .innerJoin(like)
                    .where(like.member.id.eq(getActivityDto.getId()));
            default -> jpaQueryFactory.selectFrom(article)
                    .where(article.member.id.eq(getActivityDto.getId()));
        };

        long count = query.fetch().size();
        int maxPage = (int) Math.floorDiv(count, getActivityDto.getSize());

        List<Article> fetchResult = query
                .orderBy(article.id.desc())
                .limit(getActivityDto.getSize())
                .offset(getActivityDto.getOffset())
                .fetch();

        List<ActivityListItem> activityList = fetchResult
                .stream()
                .map(Article::toActivityListItem)
                .collect(Collectors.toList());


        return ActivityListResponseDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .activityList(activityList)
                .maxPage(maxPage)
                .build();
    }

}
