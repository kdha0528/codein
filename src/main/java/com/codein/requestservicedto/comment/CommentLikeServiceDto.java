package com.codein.requestservicedto.comment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommentLikeServiceDto {

    private final Long commentId;
    private final String accessToken;
    private final boolean like;

    @Builder
    public CommentLikeServiceDto(Long commentId, String accessToken, boolean like) {
        this.commentId = commentId;
        this.accessToken = accessToken;
        this.like = like;
    }
}
