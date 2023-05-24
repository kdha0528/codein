package com.codein.requestservicedto.article;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EditArticleServiceDto {

    private final Long id;
    private final String category;
    private final String title;
    private final String content;
    private final String accessToken;

    @Builder
    public EditArticleServiceDto(Long id,String category, String title, String content,String accessToken) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.accessToken = accessToken;
    }
}
