package com.codein.repository.view;

import com.codein.domain.article.ArticleView;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleViewRepository extends JpaRepository<ArticleView, Long>, ArticleViewRepositoryCustom {
}
