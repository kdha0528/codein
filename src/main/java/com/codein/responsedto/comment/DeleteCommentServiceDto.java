package com.codein.responsedto.comment;

import lombok.Getter;

@Getter
public class DeleteCommentServiceDto {
    private final Long id;
    private final String accessToken;

    public DeleteCommentServiceDto(Long id, String accessToken) {
        this.id = id;
        this.accessToken = accessToken;
    }
}
