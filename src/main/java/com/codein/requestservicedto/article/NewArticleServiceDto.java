package com.codein.requestservicedto.article;

import com.codein.domain.article.Article;
import com.codein.domain.article.Category;
import com.codein.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NewArticleServiceDto {
    private final String category;
    private final String title;
    private final String content;

    @Builder
    public NewArticleServiceDto(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

    public Article toEntity(Member member) {
        return Article.builder()
                .member(member)
                .category(Category.valueOf(this.category))
                .title(this.title)
                .content(this.content)
                .build();

    }
}
