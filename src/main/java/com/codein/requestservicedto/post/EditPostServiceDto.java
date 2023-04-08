package com.codein.requestservicedto.post;


import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EditPostServiceDto {

    private final String category;
    private final String title;
    private final String content;

    @Builder
    public EditPostServiceDto(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }
}
