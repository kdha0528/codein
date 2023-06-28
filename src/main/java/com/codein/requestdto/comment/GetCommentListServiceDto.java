package com.codein.requestdto.comment;

import com.codein.domain.article.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

import static java.lang.Math.max;

@Getter
@ToString
public class GetCommentListServiceDto {

    private static final int SIZE = 50;
    private final Long articleId;
    private final Integer page;

    @Builder
    public GetCommentListServiceDto(Long articleId, Integer page) {
        this.articleId = articleId;
        this.page = Objects.requireNonNullElse(page, 1);
    }

    public long getOffset() {
        return (long) (max(1, page) - 1) * SIZE;
    }

    public int getSize() {
        return SIZE;
    }
}
