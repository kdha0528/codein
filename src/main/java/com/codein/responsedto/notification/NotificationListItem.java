package com.codein.responsedto.notification;

import com.codein.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

import static com.codein.utils.DateTimeUtils.toSimpleFormat;

@ToString
@Getter
public class NotificationListItem {

    private final Long id;
    private final Long senderId;
    private final String senderImageUrl;
    private final String senderNickname;
    private final Long articleId;
    private final Long commentId;

    private final String subContent;
    private final String content;

    private final boolean checked;
    private final boolean clicked;

    private final String notifiedAt;

    @Builder
    public NotificationListItem(Long id, Member sender, Long articleId, Long commentId, String subCotent, String content, boolean checked, boolean clicked, LocalDateTime notifiedAt) {
        this.id = id;
        this.senderId = sender.getId();
        this.senderNickname = sender.getNickname();
        if (sender.getProfileImage() != null) {
            this.senderImageUrl = "/my-backend-api/images/profile/" + sender.getProfileImage();
        } else {
            this.senderImageUrl = null;
        }
        this.articleId = articleId;
        this.commentId = commentId;
        this.subContent = subCotent;
        this.content = content;
        this.checked = checked;
        this.clicked = clicked;
        this.notifiedAt = toSimpleFormat(notifiedAt);
    }
}
