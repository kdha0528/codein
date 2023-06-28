package com.codein.service;

import com.codein.domain.comment.Comment;
import com.codein.domain.member.Member;
import com.codein.domain.notification.Notification;
import com.codein.domain.notification.NotificationContent;
import com.codein.error.exception.comment.CommentNotExistsException;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.error.exception.notification.NotificationNotExistsException;
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
    private final NotificationRepository notificationRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void newNotifications(NewNotificationServiceDto newNotificationServiceDto) {
        if(newNotificationServiceDto.getComment() == null) {    // 글 생성인 경우
            List<Member> receivers = memberRepository.findByFollowing(newNotificationServiceDto.getSender());
            if(!receivers.isEmpty()){
                List<Notification> notifications = new ArrayList<>();
                receivers.forEach(receiver -> {
                            notifications.add(newNotificationServiceDto.toEntity(receiver, NotificationContent.FOLLOWING_POST_NEW_ARTICLE));
                        });
                notificationRepository.saveAll(notifications);
            }
        } else {    // 댓글 생성인 경우
            List<Notification> notifications = new ArrayList<>();

            // 댓글을 단 원글 작성자에게 알림
            notifications.add(newNotificationServiceDto.toEntity(newNotificationServiceDto.getArticle().getMember(), NotificationContent.COMMENT_ON_MY_ARTICLE));

            // 대댓글인 경우 부모댓글 작성자에게 알림
            if(newNotificationServiceDto.getComment().getParentId() != null){

                Comment parent = commentRepository.findById(newNotificationServiceDto.getComment().getParentId())
                        .orElseThrow(CommentNotExistsException::new);
                Member parentMember = parent.getMember();

                // 타겟이 있는 경우 타겟에게 알림
                if(newNotificationServiceDto.getComment().getTarget() != null){
                    Member targetMember = newNotificationServiceDto.getComment().getTarget().getMember();
                    if(targetMember != parentMember) {
                        notifications.add(newNotificationServiceDto.toEntity(parentMember, NotificationContent.REPLY_ON_MY_COMMENT));
                        notifications.add(newNotificationServiceDto.toEntity(targetMember, NotificationContent.REPLY_TO_ME));
                    } else {
                        // 단 타겟과 부모가 일치할 경우 타겟 알림만 알림
                        notifications.add(newNotificationServiceDto.toEntity(parentMember, NotificationContent.REPLY_TO_ME));
                    }
                } else {
                    notifications.add(newNotificationServiceDto.toEntity(parentMember, NotificationContent.REPLY_ON_MY_COMMENT));
                }
            }
            notificationRepository.saveAll(notifications);
        }
    }

    @Transactional
    public NotificationListResponseDto getNotifications(GetNotificationsServiceDto getNotificationsServiceDto) {
        Member member = memberRepository.findByAccessToken(getNotificationsServiceDto.getAccessToken());
        if(member != null) {
            if(getNotificationsServiceDto.getLastNotificationId() == null) {    // 알림버튼을 클릭하여 처음 열람한 경우
                checkNotifications(member);     // checked가 false인 모든 알림을 true로 변경
            }
            return notificationRepository.getNotificationList(getNotificationsServiceDto, member);
        } else {
            throw new MemberNotExistsException();
        }
    }

    @Transactional
    public void checkNotifications(Member member){
        List<Notification> notifications = notificationRepository.findNotCheckedBySender(member);
        if(!notifications.isEmpty()) {
            notifications.forEach(notification ->
                    notification.setChecked(true)
            );
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
