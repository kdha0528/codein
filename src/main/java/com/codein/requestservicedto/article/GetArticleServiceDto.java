package com.codein.requestservicedto.article;

import lombok.Builder;
import lombok.Getter;

@Getter
public class GetArticleServiceDto {
    private final Long articleId;
    private final String clientIp;
    private final Integer commentPage;

    @Builder
    public GetArticleServiceDto(Long articleId, String clientIp, Integer commentPage) {
        this.articleId = articleId;
        this.clientIp = clientIp;
        this.commentPage =commentPage;
    }
}
