package com.codein.responsedto.article;

import com.codein.domain.image.ProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Random;

import static com.codein.utils.DateTimeUtils.toSimpleFormat;

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
        this.createdAt = toSimpleFormat(createdAt);
        this.authorId = authorId;
        this.nickname = nickname;
        this.viewNum = viewNum;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        this.deleted = deleted;
        if (profileImage != null) {
            this.imagePath = "https://d32r1r4pmjmgdj.cloudfront.net/images/profile/" + profileImage.getImgFileName();
        } else {
            this.imagePath = null;
        }
    }
}
