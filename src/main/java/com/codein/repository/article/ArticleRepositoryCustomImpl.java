package com.codein.repository.article;

import com.codein.domain.article.Article;
import com.codein.domain.article.ArticleLike;
import com.codein.domain.comment.Comment;
import com.codein.domain.member.Member;
import com.codein.requestdto.article.Activity;
import com.codein.requestservicedto.article.GetActivitiesServiceDto;
import com.codein.requestservicedto.article.GetArticlesServiceDto;
import com.codein.responsedto.article.ActivityListItem;
import com.codein.responsedto.article.ActivityListResponseDto;
import com.codein.responsedto.article.ArticleListItem;
import com.codein.responsedto.article.ArticleListResponseDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.management.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.codein.domain.article.QArticle.article;
import static com.codein.domain.article.QArticleLike.articleLike;
import static com.codein.domain.comment.QComment.comment;


@Repository
@RequiredArgsConstructor
public class ArticleRepositoryCustomImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Article> findByMember(Member member) {
        return jpaQueryFactory.selectFrom(article)
                .where(article.member.eq(member)
                        ,article.deleted.isFalse())
                .fetch();
    }

    @Override
    public Article findByMemberLatest(Member member) {

        return jpaQueryFactory.selectFrom(article)
                .where(
                        article.member.eq(member)
                        ,article.deleted.isFalse()
                       )
                .orderBy(article.id.desc())
                .fetchFirst();
    }

    @Override
    public ArticleListResponseDto getArticleList(GetArticlesServiceDto getArticlesServiceDto) {

        // category 와 period 조건 쿼리 추가
        JPAQuery<Article> query = jpaQueryFactory.selectFrom(article)
                .where(
                        article.category.eq(getArticlesServiceDto.getCategory()),
                        article.createdAt.between(getArticlesServiceDto.getStartDate(),LocalDateTime.now())
                        ,article.deleted.isFalse()
                );

        // 검색어 있을 시 검색
        if (getArticlesServiceDto.getKeyword() != null) {
            switch(getArticlesServiceDto.getCondition()) {
                case TITLE -> query.where(article.title.contains(getArticlesServiceDto.getKeyword()));
                case AUTHOR -> query.where(article.member.nickname.contains(getArticlesServiceDto.getKeyword()));
                default -> query.where(article.title.contains(getArticlesServiceDto.getKeyword()).or(article.content.contains(getArticlesServiceDto.getKeyword())));
            }
        }

        long count = query.fetch().size();
        int maxPage = (int) Math.floorDiv(count, getArticlesServiceDto.getSize());
        if (count % getArticlesServiceDto.getSize() != 0) {
            maxPage++;
        }

        // 정렬
        switch (getArticlesServiceDto.getSort()) {
            case VIEW -> query.orderBy(article.viewNum.desc(),article.id.desc());
            case LIKE -> query.orderBy(article.likeNum.desc(),article.id.desc());
            default -> query.orderBy(article.id.desc());
        }

        List<Article> fetchResult = query
                .limit(getArticlesServiceDto.getSize())
                .offset(getArticlesServiceDto.getOffset())
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
    public ActivityListResponseDto getActivityListResponseDto(GetActivitiesServiceDto getActivitiesServiceDto, Member member, Boolean isFollow) {

        List<Article> listArticle;
        int maxPage = 1;

        switch (getActivitiesServiceDto.getActivity()) {
            case COMMENTS -> {
                JPAQuery<Comment> subQuery = jpaQueryFactory.selectFrom(comment)
                        .where(comment.member.eq(member))
                        .orderBy(comment.id.desc());

                int size = subQuery.fetch().size();
                maxPage = Math.floorDiv(size, getActivitiesServiceDto.getSize());
                if (size % getActivitiesServiceDto.getSize() != 0) {
                    maxPage++;
                }

                List<Comment> subFetch= subQuery
                        .limit(getActivitiesServiceDto.getSize())
                        .offset(getActivitiesServiceDto.getOffset())
                        .fetch();

                listArticle = subFetch
                        .stream()
                        .map(Comment::getArticle)
                        .collect(Collectors.toList());
            }

            case LIKED_ARTICLES -> {
                JPAQuery<ArticleLike> subQuery = jpaQueryFactory.selectFrom(articleLike)
                        .where(articleLike.member.eq(member))
                        .orderBy(articleLike.id.desc());

                int size = subQuery.fetch().size();
                maxPage = Math.floorDiv(size, getActivitiesServiceDto.getSize());
                if (size % getActivitiesServiceDto.getSize() != 0) {
                    maxPage++;
                }

                List<ArticleLike> subFetch = subQuery
                        .limit(getActivitiesServiceDto.getSize())
                        .offset(getActivitiesServiceDto.getOffset())
                        .fetch();
                listArticle = subFetch
                        .stream()
                        .map(ArticleLike::getArticle)
                        .collect(Collectors.toList());
            }
            default -> {
                JPAQuery<Article> subQuery = jpaQueryFactory.selectFrom(article)
                        .where(article.member.eq(member))
                        .orderBy(article.id.desc());
                int size = subQuery.fetch().size();
                maxPage = Math.floorDiv(size, getActivitiesServiceDto.getSize());
                if (size % getActivitiesServiceDto.getSize() != 0) {
                    maxPage++;
                }

                listArticle = subQuery
                        .limit(getActivitiesServiceDto.getSize())
                        .offset(getActivitiesServiceDto.getOffset())
                        .fetch();
            }
        };

        List<ActivityListItem> activityList = listArticle
                .stream()
                .map(Article::toActivityListItem)
                .collect(Collectors.toList());

        return ActivityListResponseDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .profileImage(member.getProfileImage())
                .activityList(activityList)
                .maxPage(maxPage)
                .isFollow(isFollow)
                .build();
    }

}
