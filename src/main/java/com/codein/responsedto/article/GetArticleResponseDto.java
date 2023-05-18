package com.codein.responsedto.article;

import com.codein.domain.article.Category;
import com.codein.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class GetArticleResponseDto {
    private final Long id;
    private final Member member;
    private final Category category;
    private final String title;
    private final String content;
    private final Integer viewNum;
    private final Integer commentNum;
    private final Integer likeNum;
    private final LocalDateTime createdAt;
    private final boolean deleted;

    @Builder
    public GetArticleResponseDto(Long id, Member member, Category category, String title, String content, Integer viewNum, Integer commentNum, Integer likeNum, LocalDateTime createdAt, boolean deleted) {
        this.id = id;
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.viewNum = viewNum;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        this.createdAt = createdAt;
        this.deleted = deleted;
    }
}
