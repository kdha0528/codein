package com.codein.requestservicedto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
public class GetNotificationsServiceDto {

    private static final int SIZE = 20;
    private final String accessToken;
    private final Long lastNotificationId;

    @Builder
    public GetNotificationsServiceDto(String accessToken, Long lastNotificationId) {
        this.accessToken = accessToken;
        this.lastNotificationId = Objects.requireNonNullElse(lastNotificationId, Long.MAX_VALUE);
    }

    public int getSize() {
        return SIZE;
    }
}
