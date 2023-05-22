package com.codein.responsedto.article;

import com.codein.domain.article.Category;
import com.codein.domain.image.ProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

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
    public GetArticleResponseDto(Long id, Category category, String title, String content, Integer viewNum, Integer commentNum, Integer likeNum, LocalDateTime createdAt, Long authorId, String nickname, ProfileImage profileImage, boolean deleted) {
        this.id = id;
        this.category = category.getName();
        this.title = title;
        this.content = content;
        this.viewNum = viewNum;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        this.createdAt = compareDate(createdAt);
        this.authorId = authorId;
        this.nickname = nickname;
        if (profileImage != null) {
            this.imagePath = "/my-backend-api/images/profile/" + profileImage.getImgFileName();
        } else {
            this.imagePath = null;
        }
        this.deleted = deleted;
    }
    public String compareDate(LocalDateTime createdAt) {
        LocalDateTime now = LocalDateTime.now();
        if (now.getYear() == createdAt.getYear()) {
            if (now.getMonth() == createdAt.getMonth()) {
                if (now.getDayOfMonth() == createdAt.getDayOfMonth()) {
                    if (now.getHour() == createdAt.getHour()) {
                        if (now.getMinute() == createdAt.getMinute()) {
                            return "0분 전";
                        } else {
                            return now.compareTo(createdAt)+"분 전";
                        }
                    } else {
                        return now.compareTo(createdAt)+"시간 전";
                    }
                } else {
                    return now.compareTo(createdAt)+"일 전";
                }
            } else {
                return now.compareTo(createdAt)+"달 전";
            }
        } else {
            return now.compareTo(createdAt)+"년 전";
        }
    }
}
