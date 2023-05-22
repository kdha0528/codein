package com.codein.requestservicedto.article;

import lombok.Builder;
import lombok.Getter;
@Getter
public class ArticleLikeServiceDto {
        private final Long articleId;
        private final Long clientId;

        @Builder
        public ArticleLikeServiceDto(Long articleId, Long clientId) {
            this.articleId = articleId;
            this.clientId = clientId;
        }
}
