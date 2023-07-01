package com.codein.responsedto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class NotificationListResponseDto {

    private final List<NotificationListItem> notificationListItemList;

    @Builder
    public NotificationListResponseDto(List<NotificationListItem> notificationListItemList) {
        this.notificationListItemList = notificationListItemList;
    }
}
