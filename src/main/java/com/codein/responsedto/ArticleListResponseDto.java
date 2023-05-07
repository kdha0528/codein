package com.codein.responsedto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class ArticleListResponseDto {
    private final List<ArticleResponseDto> articleList;
    private final Integer maxPage;

    @Builder
    public ArticleListResponseDto(List<ArticleResponseDto> articleList, Integer maxPage) {
        this.articleList = articleList;
        this.maxPage = maxPage;
    }
}
