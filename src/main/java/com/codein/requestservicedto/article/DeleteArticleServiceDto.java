package com.codein.requestservicedto.article;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteArticleServiceDto {
    private final Long articleId;
    private final String accessToken;

    @Builder
    public DeleteArticleServiceDto(Long articleId, String accessToken) {
        this.articleId = articleId;
        this.accessToken = accessToken;
    }
}
