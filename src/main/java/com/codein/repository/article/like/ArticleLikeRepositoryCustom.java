package com.codein.repository.article.like;

import com.codein.domain.article.Article;
import com.codein.domain.article.ArticleLike;
import com.codein.domain.member.Member;

public interface ArticleLikeRepositoryCustom {
    ArticleLike existsArticleLike(Article article, Member member);
}
