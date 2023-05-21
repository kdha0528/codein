package com.codein.repository.like;

import com.codein.domain.article.ArticleLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long>, ArticleLikeRepositoryCustom {
}
