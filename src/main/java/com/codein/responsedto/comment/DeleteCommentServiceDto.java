package com.codein.responsedto.comment;

import lombok.Builder;
import lombok.Getter;

@Getter
public class DeleteCommentServiceDto {
    private final Long id;
    private final String accessToken;

    @Builder
    public DeleteCommentServiceDto(Long id, String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
    }
}
