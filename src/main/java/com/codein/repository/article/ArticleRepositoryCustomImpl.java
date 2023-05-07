package com.codein.repository.article;

import com.codein.domain.article.Article;
import com.codein.domain.article.Category;
import com.codein.domain.member.Member;
import com.codein.requestdto.article.GetArticlesDto;
import com.codein.responsedto.ArticleResponseDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
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
    private final EntityManager em;
    @Override
    public List<Article> findByMember(Member member) {
        return jpaQueryFactory.selectFrom(article)
                .where(article.member.eq(member))
                .fetch();
    }

    @Override
    public List<ArticleResponseDto> getArticleList(GetArticlesDto getArticlesDto, Category category) {
        LocalDateTime now = LocalDateTime.now();

        JPAQuery<Article> query = jpaQueryFactory.selectFrom(article)
                .where(
                        article.category.eq(category),
                        article.createdAt.between(getArticlesDto.getStartDate(),now)
                )
                .limit(getArticlesDto.getSize())
                .offset(getArticlesDto.getOffset());

        switch (getArticlesDto.getSort()) {
            case VIEW -> query.orderBy(article.viewNum.desc(), article.id.desc());
            case LIKE -> query.orderBy(article.likeNum.desc(), article.id.desc());
            default -> query.orderBy(article.id.desc());
        }

       List<Article> articleList = query.fetch();

        return articleList.stream()
                .map(Article::toArticleListResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Integer getMaxPage(GetArticlesDto getArticlesDto, Category category) {

        String jpql = "SELECT COUNT(article) FROM Article article WHERE " +
                "article.category = :category " +
                "AND article.createdAt BETWEEN :startDate AND :endDate";
        Query query = em.createQuery(jpql);
        query.setParameter("category", category);
        query.setParameter("startDate", getArticlesDto.getStartDate());
        query.setParameter("endDate", LocalDateTime.now());
        Long count =  (Long) query.getSingleResult();
        return count.intValue();
    }
}
