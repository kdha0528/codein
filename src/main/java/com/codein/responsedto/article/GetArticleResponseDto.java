package com.codein.responsedto.article;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class GetArticleResponseDto {
    private final Long id;
    private final String category;
    private final String title;
    private final String content;
    private final Integer viewNum;
    private final Integer commentNum;
    private final Integer likeNum;
    private final String createdAt;
    private final Long authorId;
    private final String nickname;
    private final String imagePath;
    private final boolean deleted;

    @Builder
    public GetArticleResponseDto(Long id, String category, String title, String content, Integer viewNum, Integer commentNum, Integer likeNum, String createdAt, Long authorId, String nickname, String imagePath, boolean deleted) {
        this.id = id;
        this.category = category;
        this.title = title;
        this.content = content;
        this.viewNum = viewNum;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        this.createdAt = createdAt;
        this.authorId = authorId;
        this.nickname = nickname;
        this.imagePath = imagePath;
        this.deleted = deleted;
    }
}
