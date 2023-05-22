package com.codein.requestservicedto.article;

import lombok.Getter;

@Getter
public class GetArticleServiceDto {
    private final Long articleId;
    private final String clientIp;

    public GetArticleServiceDto(Long articleId, String clientIp) {
        this.articleId = articleId;
        this.clientIp = clientIp;
    }
}
