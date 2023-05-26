package com.codein.responsedto.comment;

import com.codein.domain.image.ProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ToString
@Getter
public class CommentListItem {

    private final Long id;
    private final String content;
    private final String createdAt;
    private final Long articleId;
    private final Long commenterId;
    private final String commenterNickname;
    private final String commenterImagePath;
    private final Long targetId;
    private final String targetNickname;

    @Builder
    public CommentListItem(Long id, String content, LocalDateTime createdAt, Long articleId, Long commenterId, String commenterNickname, ProfileImage commenterProfileImage, Long targetId, String targetNickname) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        this.id = id;
        this.content = content;
        this.createdAt = createdAt.format(formatter);
        this.articleId = articleId;
        this.commenterId = commenterId;
        this.commenterNickname = commenterNickname;
        if (commenterProfileImage != null) {
            this.commenterImagePath = "/my-backend-api/images/profile/" + commenterProfileImage.getImgFileName();
        } else {
            this.commenterImagePath = null;
        }
        this.targetId = targetId;
        this.targetNickname = targetNickname;
    }
}
