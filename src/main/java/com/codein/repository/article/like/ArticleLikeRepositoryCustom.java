package com.codein.repository.article.like;

import com.codein.requestservicedto.article.ArticleLikeServiceDto;

public interface ArticleLikeRepositoryCustom {
    boolean existsArticleLike(ArticleLikeServiceDto articleLikeServiceDto);
}
