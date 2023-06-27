package com.codein.service;

import com.codein.domain.member.Member;
import com.codein.domain.notification.Notification;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.error.exception.notification.NotificationNotExistsException;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.comment.CommentRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.repository.notification.NotificationRepository;
import com.codein.requestservicedto.notification.ClickNotificationServiceDto;
import com.codein.requestservicedto.notification.GetNotificationsServiceDto;
import com.codein.requestservicedto.notification.NewNotificationServiceDto;
import com.codein.responsedto.notification.NotificationListResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final NotificationRepository notificationRepository;

    @Transactional
    public List<Notification> newNotifications(NewNotificationServiceDto newNotificationServiceDto) {
        if(newNotificationServiceDto.getReceivers().isEmpty()) {
            return null;
        } else {
            List<Notification> notifications = new ArrayList<>();
            newNotificationServiceDto.getReceivers().forEach(receiver ->
                    notifications.add(newNotificationServiceDto.toEntity(receiver))
            );
            notificationRepository.saveAll(notifications);
            return notifications;
        }
    }


    @Transactional
    public NotificationListResponseDto getNotifications(GetNotificationsServiceDto getNotificationsServiceDto) {

        Member member = memberRepository.findByAccessToken(getNotificationsServiceDto.getAccessToken());
        if(member != null) {
            return notificationRepository.getNotificationList(getNotificationsServiceDto, member);
        }else{
            throw new MemberNotExistsException();
        }
    }

    @Transactional
    public void checkNotifications(String accessToken){
        Member member = memberRepository.findByAccessToken(accessToken);
        if(member != null){
            List<Notification> notifications = notificationRepository.findNotCheckedBySender(member);
            notifications.forEach(notification ->
                    notification.setChecked(true)
            );
        }else{
            throw new MemberNotExistsException();
        }
    }

    @Transactional
    public void clickNotification(ClickNotificationServiceDto clickNotificationServiceDto){
        Member member = memberRepository.findByAccessToken(clickNotificationServiceDto.getAccessToken());
        if(member != null){
            Notification notification = notificationRepository.findById(clickNotificationServiceDto.getNotificationId())
                    .orElseThrow(NotificationNotExistsException::new);
            if(!notification.isClicked()) notification.setClicked(true);

        } else {
            throw new MemberNotExistsException();
        }
    }
}
