package com.codein.domain.notification;

import com.codein.domain.article.Article;
import com.codein.domain.member.Member;
import com.codein.responsedto.notification.NotificationListItem;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member receiver;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    @NotNull
    private NotificationContent content;

    @NotNull
    private boolean checked;
    @NotNull
    private boolean clicked;

    @NotNull
    private LocalDateTime notifiedAt;

    @Builder
    public Notification(Member sender, Member receiver, Article article, NotificationContent content) {
        this.sender = sender;
        this.receiver = receiver;
        this.article = article;
        this.content = content;
        this.checked = false;
        this.clicked = false;
        this.notifiedAt = LocalDateTime.now();
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public NotificationListItem toNotificationListItem(){
        return NotificationListItem.builder()
                .id(this.id)
                .receiverId(this.receiver.getId())
                .senderId(this.sender.getId())
                .articleId(this.article.getId())
                .content(this.content.ordinal())
                .checked(isChecked())
                .clicked(isClicked())
                .notifiedAt(this.notifiedAt)
                .build();
    }
}