package com.codein.requestservicedto.article;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EditArticleServiceDto {

    private final String category;
    private final String title;
    private final String content;

    @Builder
    public EditArticleServiceDto(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }
}
