package com.codein.repository.article.like;

import com.codein.domain.article.Article;
import com.codein.domain.article.ArticleLike;
import com.codein.domain.member.Member;
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
    public ArticleLike existsArticleLike(Article article, Member member) {

        return jpaQueryFactory.selectFrom(articleLike)
                .where(articleLike.article.eq(article)
                        .and(articleLike.member.eq(member)))
                .fetchFirst();
    }


}
