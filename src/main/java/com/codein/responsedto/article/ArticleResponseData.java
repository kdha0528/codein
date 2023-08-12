package com.codein.responsedto.article;

import com.codein.domain.article.Category;
import com.codein.domain.image.ProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

import static com.codein.utils.DateTimeUtils.toNormalFormat;

@ToString
@Getter
public class ArticleResponseData {
    private final Long id;
    private final String category;
    private final String title;
    private final String content;
    private final Integer viewNum;
    private final Integer commentNum;
    private final Integer likeNum;
    private final Integer dislikeNum;
    private final String createdAt;
    private final Long authorId;
    private final String nickname;
    private final String imagePath;
    private final boolean deleted;

    @Builder
    public ArticleResponseData(Long id, Category category, String title, String content, Integer viewNum, Integer commentNum, Integer likeNum, Integer dislikeNum, LocalDateTime createdAt, Long authorId, String nickname, ProfileImage profileImage, boolean deleted) {
        this.id = id;
        this.category = category.getName();
        this.title = title;
        this.content = content;
        this.viewNum = viewNum;
        this.commentNum = commentNum;
        this.likeNum = likeNum;
        this.dislikeNum = dislikeNum;
        this.createdAt = toNormalFormat(createdAt);
        this.authorId = authorId;
        this.nickname = nickname;
        if (profileImage != null) {
            this.imagePath = "https://d32r1r4pmjmgdj.cloudfront.net/images/profile/" + profileImage.getImgFileName();
        } else {
            this.imagePath = null;
        }
        this.deleted = deleted;
    }

}
