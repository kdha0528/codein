package com.codein.requestservicedto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class GetNotificationsServiceDto {

    private static final int SIZE = 20;
    private final String accessToken;
    private final Long lastNotificationId;

    @Builder
    public GetNotificationsServiceDto(String accessToken, Long lastNotificationId) {
        this.accessToken = accessToken;
        this.lastNotificationId = lastNotificationId;
    }

    public int getSize() {
        return SIZE;
    }
}
