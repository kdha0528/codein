package com.codein.requestdto.comment;

import com.codein.domain.article.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import static java.lang.Math.max;

@Getter
@ToString
public class GetCommentListServiceDto {

    private static final int SIZE = 50;
    private final Article article;
    private final Integer page;

    @Builder
    public GetCommentListServiceDto(Article article, Integer page) {
        this.article = article;
        this.page = page;
    }

    public long getOffset() {
        return (long) (max(1, page) - 1) * SIZE;
    }

    public int getSize() {
        return SIZE;
    }
}
