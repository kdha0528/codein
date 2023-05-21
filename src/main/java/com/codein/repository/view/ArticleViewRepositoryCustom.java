package com.codein.repository.view;


import com.codein.domain.article.Article;

public interface ArticleViewRepositoryCustom {
    void addView(Article article, String clientIp);
}
