package com.codein.requestservicedto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class ClickNotificationServiceDto {
    private final String accessToken;
    private final Long notificationId;

    @Builder
    public ClickNotificationServiceDto(String accessToken, Long notificationId) {
        this.accessToken = accessToken;
        this.notificationId = notificationId;
    }
}
