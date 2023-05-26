package com.codein.responsedto.comment;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class CommentListResponseDto {

    private final List<CommentListItem> commentList;
    private final int maxPage;

    @Builder
    public CommentListResponseDto(List<CommentListItem> commentList, int maxPage) {
        this.commentList = commentList;
        this.maxPage = maxPage;
    }
}
