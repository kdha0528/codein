package com.codein.requestservicedto.notification;

import com.codein.domain.article.Article;
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

    private final NotificationContent content;
    private final Member sender;
    private final List<Member> receivers;
    private final Article article;

    @Builder
    public NewNotificationServiceDto(NotificationContent content, Member sender, List<Member> receivers, Article article) {
        this.content = content;
        this.sender = sender;
        this.receivers = receivers;
        this.article = article;
    }

    public Notification toEntity(Member receiver) {
        return Notification.builder()
                .sender(this.sender)
                .receiver(receiver)
                .content(this.content)
                .build();
    }


}
