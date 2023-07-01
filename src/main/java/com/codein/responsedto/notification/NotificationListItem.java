package com.codein.responsedto.notification;

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
    private final Long receiverId;
    private final Long articleId;
    private final Long commentId;
    private final Integer content;

    private final boolean checked;
    private final boolean clicked;

    private final String notifiedAt;

    @Builder
    public NotificationListItem(Long id, Long senderId, Long receiverId, Long articleId, Long commentId, Integer content, boolean checked, boolean clicked, LocalDateTime notifiedAt) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.articleId = articleId;
        this.commentId = commentId;
        this.content = content;
        this.checked = checked;
        this.clicked = clicked;
        this.notifiedAt = toSimpleFormat(notifiedAt);
    }
}
