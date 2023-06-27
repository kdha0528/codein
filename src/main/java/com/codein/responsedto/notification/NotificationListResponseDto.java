package com.codein.responsedto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class NotificationListResponseDto {

    private final List<NotificationListItem> notificationListItemList;
    private final int notChecked;

    @Builder
    public NotificationListResponseDto(List<NotificationListItem> notificationListItemList, int notChecked) {
        this.notificationListItemList = notificationListItemList;
        this.notChecked = notChecked;
    }
}
