package com.codein.responsedto.article;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.codein.utils.DateTimeUtils.toNormalFormat;

@ToString
@Getter
public class ActivityListItem {
    private final Long id;
    private final String category;
    private final String title;
    private final String createdAt;
    private final Long authorId;
    private final String nickname;
    private final Integer viewNum;
    private final Integer commentNum;
    private final Integer likeNum;
    private final boolean deleted;

    @Builder
    public ActivityListItem(Long id, String category, String title, LocalDateTime createdAt, Long authorId, String nickname, Integer viewNum,Integer commentNum,Integer likeNum,boolean deleted) {

        this.id = id;
        this.category = category;
        this.title = title;
        this.createdAt = toNormalFormat(createdAt);
        this.authorId = authorId;
        this.nickname = nickname;
        this.viewNum = viewNum;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        this.deleted = deleted;
    }

}
