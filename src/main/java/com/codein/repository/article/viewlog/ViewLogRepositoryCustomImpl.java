package com.codein.repository.article.viewlog;

import com.codein.domain.article.ViewLog;
import com.codein.requestservicedto.article.GetArticleServiceDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codein.domain.article.QViewLog.viewLog;

@Repository
@RequiredArgsConstructor
public class ViewLogRepositoryCustomImpl implements ViewLogRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public ViewLog findByGetArticleServiceDto(GetArticleServiceDto getArticleServiceDto) {

        return jpaQueryFactory.selectFrom(viewLog)
                .where(viewLog.article.id.eq(getArticleServiceDto.getArticleId())
                        ,viewLog.clientIp.eq(getArticleServiceDto.getClientIp()))
                .orderBy(viewLog.id.desc())
                .fetchFirst();
    }
}
