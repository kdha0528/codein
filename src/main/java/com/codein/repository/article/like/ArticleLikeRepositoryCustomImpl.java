package com.codein.repository.article.like;

import com.codein.domain.article.ArticleLike;
import com.codein.requestservicedto.article.ArticleLikeServiceDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codein.domain.article.QArticleLike.articleLike;

@Repository
@RequiredArgsConstructor
public class ArticleLikeRepositoryCustomImpl implements ArticleLikeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public boolean existsArticleLike(ArticleLikeServiceDto articleLikeServiceDto) {

        ArticleLike fetchResult = jpaQueryFactory.selectFrom(articleLike)
                .where(articleLike.article.id.eq(articleLikeServiceDto.getArticleId())
                        .and(articleLike.member.eq(articleLikeServiceDto.getMember())))
                .fetchFirst();

        return fetchResult != null;

    }
}
