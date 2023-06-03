package com.codein.responsedto.article;

import com.codein.responsedto.comment.CommentListResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class GetArticleResponseDto {
    private final ArticleResponseData articleData;
    private final CommentListResponseDto commentsData;

    @Builder
    public GetArticleResponseDto(ArticleResponseData articleData, CommentListResponseDto commentsData) {
        this.articleData = articleData;
        this.commentsData = commentsData;
    }

}
