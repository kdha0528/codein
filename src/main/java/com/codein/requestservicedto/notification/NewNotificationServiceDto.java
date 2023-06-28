package com.codein.requestservicedto.notification;

import com.codein.domain.article.Article;
import com.codein.domain.comment.Comment;
import com.codein.domain.member.Member;
import com.codein.domain.notification.Notification;
import com.codein.domain.notification.NotificationContent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class NewNotificationServiceDto {

    private final Member sender;
    private final Article article;
    private final Comment comment;

    @Builder
    public NewNotificationServiceDto(Member sender, Article article, Comment comment) {
        this.sender = sender;
        this.article = article;
        this.comment = comment;
    }

    public Notification toEntity(Member receiver, NotificationContent content) {
        return Notification.builder()
                .sender(this.sender)
                .receiver(receiver)
                .content(content)
                .build();
    }


}
