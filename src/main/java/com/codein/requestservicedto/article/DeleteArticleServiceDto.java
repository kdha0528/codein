package com.codein.requestservicedto.article;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteArticleServiceDto {
    private final Long id;
    private final String accessToken;

    @Builder
    public DeleteArticleServiceDto(Long id, String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
    }
}
