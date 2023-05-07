package com.codein.responsedto;

import com.codein.domain.image.ProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
public class ArticleResponseDto {
    private final Long id;
    private final String title;
    private final String createdAt;
    private final Integer viewNum;
    private final Integer commentNum;
    private final Integer likeNum;
    private final String nickname;
    private final String imagePath;

    @Builder
    public ArticleResponseDto(Long id, String title, LocalDateTime createdAt, Integer viewNum, Integer commentNum, Integer likeNum, String nickname, ProfileImage profileImage) {
        this.id = id;
        this.title = title;
        this.createdAt = compareDate(createdAt);
        this.viewNum = viewNum;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        this.nickname = nickname;
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
