package com.codein.repository.notification;

import com.codein.domain.member.Member;
import com.codein.domain.notification.Notification;
import com.codein.requestservicedto.notification.GetNotificationsServiceDto;
import com.codein.responsedto.notification.NotificationListResponseDto;

import java.util.List;

public interface NotificationRepositoryCustom {
    List<Notification> findNotCheckedBySender(Member member);
    NotificationListResponseDto getNotificationList(GetNotificationsServiceDto getNotificationsServiceDto, Member member);
}
