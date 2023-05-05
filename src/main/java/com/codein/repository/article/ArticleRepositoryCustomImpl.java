package com.codein.repository.article;

import com.codein.domain.article.Article;
import com.codein.domain.article.Category;
import com.codein.domain.member.Member;
import com.codein.requestdto.PageSizeDto;
import com.codein.responsedto.ArticleListResponseDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.codein.domain.article.QArticle.article;


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
    public List<ArticleListResponseDto> getArticleList(PageSizeDto pageSizeDto, Category category) {
        LocalDateTime now = LocalDateTime.now();

        JPAQuery<Article> query = jpaQueryFactory.selectFrom(article)
                .where(
                        article.category.eq(category),
                        article.createdAt.between(pageSizeDto.getStartDate(),now)
                )
                .limit(pageSizeDto.getSize())
                .offset(pageSizeDto.getOffset());

        switch (pageSizeDto.getSort()) {
            case "view" -> query.orderBy(article.viewNum.desc(), article.id.desc());
            case "like" -> query.orderBy(article.likeNum.desc(), article.id.desc());
            default -> query.orderBy(article.id.desc());
        }

       List<Article> articleList = query.fetch();

        return articleList.stream()
                .map(Article::toArticleListResponseDto)
                .collect(Collectors.toList());
    }
}
