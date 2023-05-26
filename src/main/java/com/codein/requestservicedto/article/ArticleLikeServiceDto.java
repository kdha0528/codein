package com.codein.requestservicedto.article;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleLikeServiceDto {

        private final Long articleId;
        private final String accessToken;
        private final boolean like;

        @Builder
        public ArticleLikeServiceDto(Long articleId,  String accessToken, boolean isLike) {
            this.articleId = articleId;
            this.accessToken = accessToken;
            this.like = isLike;
        }
}
