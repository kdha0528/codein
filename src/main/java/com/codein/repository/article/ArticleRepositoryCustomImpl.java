package com.codein.repository.article;

import com.codein.domain.article.Article;
import com.codein.domain.article.Category;
import com.codein.domain.member.Member;
import com.codein.requestdto.article.GetArticlesDto;
import com.codein.responsedto.ArticleListItem;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
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
    public List<ArticleListItem> getArticleList(GetArticlesDto getArticlesDto, Category category) {

        JPAQuery<Article> query = jpaQueryFactory.selectFrom(article)
                .where(
                        article.category.eq(category),
                        article.createdAt.between(getArticlesDto.getStartDate(),LocalDateTime.now())
                )
                .limit(getArticlesDto.getSize())
                .offset(getArticlesDto.getOffset());

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

       List<Article> articleList = query.fetch();

        return articleList.stream()
                .map(Article::toArticleListResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public int getMaxPage(GetArticlesDto getArticlesDto, Category category) {

        long count = jpaQueryFactory.selectFrom(article)
                .where(
                        article.category.eq(category),
                        article.createdAt.between(getArticlesDto.getStartDate(), LocalDateTime.now()))
                .fetch()
                .size();
        /*
        String jpql = "SELECT COUNT(article) FROM Article article WHERE " +
                "article.category = :category " +
                "AND article.createdAt BETWEEN :startDate AND :endDate";
        Query query = em.createQuery(jpql);
        query.setParameter("category", category);
        query.setParameter("startDate", getArticlesDto.getStartDate());
        query.setParameter("endDate", LocalDateTime.now());
        Long count =  (Long) query.getSingleResult();*/
        return (int)count/20; // 한 페이지에 20개
    }
}
