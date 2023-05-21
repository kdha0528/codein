package com.codein.repository.article;

import com.codein.domain.article.Article;
import com.codein.domain.member.Member;
import com.codein.requestservicedto.article.GetActivitiesServiceDto;
import com.codein.requestservicedto.article.GetArticlesServiceDto;
import com.codein.responsedto.article.ActivityListItem;
import com.codein.responsedto.article.ActivityListResponseDto;
import com.codein.responsedto.article.ArticleListItem;
import com.codein.responsedto.article.ArticleListResponseDto;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.codein.domain.article.QArticle.article;
import static com.codein.domain.article.QArticleLike.articleLike;
import static com.codein.domain.article.QArticleView.articleView;
import static com.codein.domain.comment.QComment.comment;
import static com.querydsl.core.types.ExpressionUtils.count;


@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Article> findByMember(Member member) {
        return jpaQueryFactory.select(article)
                .where(article.member.eq(member))
                .fetch();
    }

    @Override
    public ArticleListResponseDto getArticleList(GetArticlesServiceDto getArticlesServiceDto) {

        JPAQuery<Article> query = jpaQueryFactory.select(article)
                .from(article)
                .where(
                        article.category.eq(getArticlesServiceDto.getCategory()),
                        article.createdAt.between(getArticlesServiceDto.getStartDate(),LocalDateTime.now())
                );

        long count = query.fetch().size();
        int maxPage = (int) Math.floorDiv(count, getArticlesServiceDto.getSize());
        if (count % getArticlesServiceDto.getSize() != 0) {
            maxPage++;
        }

        if (getArticlesServiceDto.getKeyword() != null) {
            switch(getArticlesServiceDto.getCondition()) {
                case TITLE -> query.where(article.title.contains(getArticlesServiceDto.getKeyword()));
                case AUTHOR -> query.where(article.member.nickname.contains(getArticlesServiceDto.getKeyword()));
                default -> query.where(article.title.contains(getArticlesServiceDto.getKeyword()).or(article.content.contains(getArticlesServiceDto.getKeyword())));
            }
        }

        switch (getArticlesServiceDto.getSort()) {
            case VIEW -> query
                    .leftJoin(articleView)
                    .on(article.id.eq(articleLike.article.id))
                    .groupBy(article.id)
                    .orderBy(article.id.count().nullif(0L).desc(), article.id.desc());

            case LIKE -> query
                    .leftJoin(articleLike)
                    .on(article.id.eq(articleLike.article.id))
                    .groupBy(article.id)
                    .orderBy(article.id.count().nullif(0L).desc(), article.id.desc());

            default -> query.orderBy(article.id.desc());
        }

        List<Article> fetchResult = query
                .limit(getArticlesServiceDto.getSize())
                .offset(getArticlesServiceDto.getOffset())
                .fetch();

        System.out.println("result = " + fetchResult);

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
    public ActivityListResponseDto getActivityListResponseDto(GetActivitiesServiceDto getActivitiesServiceDto, Member member) {

        JPAQuery<Article> query = switch (getActivitiesServiceDto.getActivity()) {
            case COMMENTS -> jpaQueryFactory.select(comment.article)
                    .innerJoin(comment)
                    .where(comment.commenter.id.eq(getActivitiesServiceDto.getId()));
            case LIKED_ARTICLES -> jpaQueryFactory.select(articleLike.article)
                    .innerJoin(articleLike)
                    .where(articleLike.member.id.eq(getActivitiesServiceDto.getId()));
            default -> jpaQueryFactory.selectFrom(article)
                    .where(article.member.id.eq(getActivitiesServiceDto.getId()));
        };

        long count = query.fetch().size();
        int maxPage = (int) Math.floorDiv(count, getActivitiesServiceDto.getSize());

        List<Article> fetchResult = query
                .orderBy(article.id.desc())
                .limit(getActivitiesServiceDto.getSize())
                .offset(getActivitiesServiceDto.getOffset())
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
