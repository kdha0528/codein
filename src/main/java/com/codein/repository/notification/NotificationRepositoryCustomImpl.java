package com.codein.repository.notification;

import com.codein.domain.member.Member;
import com.codein.domain.notification.Notification;
import com.codein.requestservicedto.notification.GetNotificationsServiceDto;
import com.codein.responsedto.notification.NotificationListItem;
import com.codein.responsedto.notification.NotificationListResponseDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.codein.domain.notification.QNotification.notification;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Notification> findNotCheckedBySender(Member member) {

        return jpaQueryFactory.selectFrom(notification)
                .where(notification.sender.eq(member), notification.checked.isFalse())
                .fetch();
    }

    @Override
    public NotificationListResponseDto getNotificationList(GetNotificationsServiceDto getNotificationsServiceDto, Member member) {
         JPAQuery<Notification> query = jpaQueryFactory.selectFrom(notification)
                .where(notification.receiver.eq(member), notification.id.lt(getNotificationsServiceDto.getLastNotificationId()))
                .orderBy(notification.id.desc());

         int notChecked = query
                 .where(notification.checked.isFalse())
                 .fetch()
                 .size();

        List<Notification> fetchResult = query
                .limit(getNotificationsServiceDto.getSize())
                .fetch();

        List<NotificationListItem> notificationList = fetchResult
                .stream()
                .map(Notification::toNotificationListItem)
                .collect(Collectors.toList());

        return NotificationListResponseDto.builder()
                .notificationListItemList(notificationList)
                .notChecked(notChecked)
                .build();
    }
}

