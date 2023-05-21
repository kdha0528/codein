package com.codein.repository.view;


import com.codein.domain.article.Article;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor
public class ArticleViewRepositoryCustomImpl implements ArticleViewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public void addView(Article article, String clientIp) {
        // 1분 내에 게시물을 조회한 적이 있는 ip인지 체크

    }
}
