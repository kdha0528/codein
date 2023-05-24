package com.codein.requestservicedto.article;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ArticleLikeServiceDto {
        private final Long articleId;
        private final String accessToken;

        @Builder
        public ArticleLikeServiceDto(Long articleId,  String accessToken) {
            this.articleId = articleId;
            this.accessToken = accessToken;
        }
}
