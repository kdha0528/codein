package com.codein.requestservicedto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EditCommentServiceDto  {

    private final Long id;
    private final String content;
    private final String accessToken;

    @Builder
    public EditCommentServiceDto(Long id, String content, String accessToken) {
        this.id = id;
        this.content = content;
        this.accessToken = accessToken;
    }
}
