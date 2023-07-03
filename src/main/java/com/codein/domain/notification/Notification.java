package com.codein.domain.notification;

import com.codein.domain.article.Article;
import com.codein.domain.comment.Comment;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment comment;

    @NotNull
    private NotificationContent content;

    @NotNull
    private boolean checked;
    @NotNull
    private boolean clicked;

    @NotNull
    private LocalDateTime notifiedAt;

    @Builder
    public Notification(Member sender, Member receiver, Article article, Comment comment, NotificationContent content) {
        this.sender = sender;
        this.receiver = receiver;
        this.article = article;
        this.comment = comment;
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
                .sender(this.sender)
                .articleId(this.article.getId())
                .commentId(this.comment.getId())
                .subCotent(getNotificationSubContent())
                .content(getNotificationContent())
                .checked(isChecked())
                .clicked(isClicked())
                .notifiedAt(this.notifiedAt)
                .build();
    }

    public String getNotificationContent() {
        String content;
        if(this.content.ordinal() == 0) {
            content = this.getArticle().getTitle().substring(0,30);
        } else {
            content = this.getComment().getContent().substring(0,30);
        }
        return content;
    }

    public String getNotificationSubContent() {
        return switch (this.content.ordinal()) {
            case 0 -> "새 글 작성";
            case 1 -> "내 글의 댓글";
            case 2 -> "내 댓글에 답글";
            case 3 -> "나에게 멘션";
            default -> throw new RuntimeException();
        };
    }
}
