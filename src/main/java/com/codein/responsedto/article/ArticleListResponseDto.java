package com.codein.responsedto.article;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class ArticleListResponseDto {
    private final List<ArticleListItem> articleList;
    private final int maxPage;

    @Builder
    public ArticleListResponseDto(List<ArticleListItem> articleList, int maxPage) {
        this.articleList = articleList;
        this.maxPage = maxPage;
    }
}
