package com.codein.responsedto.comment;

import com.codein.domain.image.ProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

import static com.codein.utils.DateTimeUtils.toSimpleFormat;

@ToString
@Getter
public class CommentListItem {

    private final Long id;
    private final Long parentId;
    private final String content;
    private final String createdAt;
    private final Integer likeNum;
    private final Integer dislikeNum;

    private final Long commenterId;
    private final String commenterNickname;
    private final String commenterImagePath;

    private final Long targetMemberId;
    private final String targetNickname;

    private final boolean deleted;

    @Builder
    public CommentListItem(Long id, Long parentId, String content, LocalDateTime createdAt, Integer likeNum, Integer dislikeNum, Long commenterId, String commenterNickname, ProfileImage commenterProfileImage, Long targetMemberId, String targetNickname, boolean deleted) {
        this.id = id;
        this.parentId = parentId;
        this.content = content;
        this.createdAt = toSimpleFormat(createdAt);
        this.likeNum = likeNum;
        this.dislikeNum = dislikeNum;
        this.commenterId = commenterId;
        this.commenterNickname = commenterNickname;
        if (commenterProfileImage != null) {
            this.commenterImagePath = "/my-backend-api/images/profile/" + commenterProfileImage.getImgFileName();
        } else {
            this.commenterImagePath = null;
        }
        this.targetMemberId = targetMemberId;
        this.targetNickname = targetNickname;
        this.deleted = deleted;
    }
}
