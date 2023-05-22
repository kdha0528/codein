package com.codein.responsedto.article;

import com.codein.domain.image.ProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Random;

@ToString
@Getter
public class ArticleListItem {
    private final Long id;
    private final String title;
    private final String createdAt;
    private final Long authorId;
    private final String nickname;
    private final String imagePath;
    private final Integer viewNum;
    private final Integer commentNum;
    private final Integer likeNum;
    private final boolean deleted;

    @Builder
    public ArticleListItem(Long id, String title, LocalDateTime createdAt, Long authorId, String nickname, ProfileImage profileImage, Integer viewNum, Integer commentNum, Integer likeNum, boolean deleted) {
        this.id = id;
        this.title = title;
        this.createdAt = compareDate(createdAt);
        this.authorId = authorId;
        this.nickname = nickname;
        this.viewNum = viewNum;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        this.deleted = deleted;
        if (profileImage != null) {
            this.imagePath = "/my-backend-api/images/profile/" + profileImage.getImgFileName();
        } else {
            this.imagePath = null;
        }
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
