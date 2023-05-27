package com.codein.repository.article.like;

import com.codein.domain.article.Article;
import com.codein.domain.article.ArticleLike;
import com.codein.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Long>, ArticleLikeRepositoryCustom {

}
