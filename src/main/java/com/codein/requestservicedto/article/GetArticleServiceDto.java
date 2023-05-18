package com.codein.requestservicedto.article;

import com.codein.domain.article.Category;
import com.codein.domain.member.Member;
import com.codein.responsedto.article.GetArticleResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class GetArticleServiceDto {
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
    public GetArticleServiceDto(Long id, Member member, Category category, String title, String content, Integer viewNum, Integer commentNum, Integer likeNum, LocalDateTime createdAt, boolean deleted) {
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

    public GetArticleResponseDto toGetArticleResponseDto () {
        return GetArticleResponseDto.builder()
                .id(this.id)
                .deleted(this.deleted)
                .category(this.category)
                .commentNum(this.commentNum)
                .content(this.content)
                .createdAt(this.createdAt)
                .likeNum(this.likeNum)
                .member(this.member)
                .title(this.title)
                .viewNum(this.viewNum)
                .build();
    }
}
