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
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final CommentRepository commentRepository;

/*    @Transactional
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
        }
        else {    // 댓글 생성인 경우
            List<Notification> notifications = new ArrayList<>();

            Member author = newNotificationServiceDto.getArticle().getMember();

            // 댓글을 단 원글 작성자에게 알림
            if(!newNotificationServiceDto.getSender().equals(newNotificationServiceDto.getArticle().getMember())) {
                notifications.add(newNotificationServiceDto.toEntity(author, NotificationContent.COMMENT_ON_MY_ARTICLE));
            }

            // 대댓글인 경우 부모댓글 작성자에게 알림
            if(newNotificationServiceDto.getComment().getParentId() != null) {
                Comment parent = commentRepository.findById(newNotificationServiceDto.getComment().getParentId())
                        .orElseThrow(CommentNotExistsException::new);
                Member parentMember = parent.getMember();
                //  부모댓글이 본인인 경우 알림 제외, 타겟이 타인인 경우 타겟에게 알림
                if(!parentMember.equals(newNotificationServiceDto.getSender())) {
                    notifications.add(newNotificationServiceDto.toEntity(parentMember, NotificationContent.REPLY_ON_MY_COMMENT));
                    // 타겟이 있는 경우 타겟에게 알림 단 타겟이 본인인 경우 제외
                    if (newNotificationServiceDto.getComment().getTarget() != null) {
                        Member targetMember = newNotificationServiceDto.getComment().getTarget().getMember();

                        if (!targetMember.equals(newNotificationServiceDto.getSender())) {
                            notifications.add(newNotificationServiceDto.toEntity(targetMember, NotificationContent.REPLY_TO_ME));
                        }
                    }
                }
            }

            Collections.reverse(notifications);
            notificationRepository.saveAll( removeDuplicateReceivers(notifications));
        }
    }*/

    @Transactional
    public void newNotifications(NewNotificationServiceDto newNotificationServiceDto) {
        Member sender = newNotificationServiceDto.getSender();
        if (newNotificationServiceDto.getComment() == null) {    // 글 생성인 경우
            List<Member> receivers = memberRepository.findByFollowing(sender);
            if (!receivers.isEmpty()) {
                List<Notification> notifications = new ArrayList<>();
                receivers.forEach(receiver -> {
                    notifications.add(newNotificationServiceDto.toEntity(receiver, NotificationContent.FOLLOWING_POST_NEW_ARTICLE));
                });
                notificationRepository.saveAll(notifications);
            }
        } else {    // 댓글 생성인 경우
            List<Notification> notifications = new ArrayList<>();

            Member author = newNotificationServiceDto.getArticle().getMember();

            Comment target = newNotificationServiceDto.getComment().getTarget();
            Member parentMember = null;

            if (newNotificationServiceDto.getComment().getParentId() != null) {
                Comment parentComment = commentRepository.findById(newNotificationServiceDto.getComment().getParentId())
                        .orElseThrow(CommentNotExistsException::new);

                parentMember = parentComment.getMember();
            }
            // target member가 존재하면 target member에게 알림
            if (target != null) {
                Member targetMember = target.getMember();
                if (isNotSender(sender, targetMember)) { // sender와 receiver가 동일하면 알림 x
                    notifications.add(newNotificationServiceDto.toEntity(targetMember, NotificationContent.REPLY_TO_ME));
                    if(parentMember != null ) {
                        // target member와 parent member가 일치하지 않는다면 parent member에게 알림
                        if (!parentMember.getId().equals(targetMember.getId()) && isNotSender(sender, parentMember)) {
                            notifications.add(newNotificationServiceDto.toEntity(parentMember, NotificationContent.REPLY_ON_MY_COMMENT));
                        }

                    }

                }
                if(parentMember != null ) {
                    // author가 target member, parent member와 일치하지 않는다면 author에게 알림
                    if (isNotSender(sender, author) && !author.getId().equals(targetMember.getId()) && !author.getId().equals(parentMember.getId())) {
                        notifications.add(newNotificationServiceDto.toEntity(author, NotificationContent.COMMENT_ON_MY_ARTICLE));
                    }
                }
            } else {
                // parent member가 존재한다면 parent member에게 알림
                if (parentMember != null ) {
                    if(isNotSender(sender, parentMember)) {
                        notifications.add(newNotificationServiceDto.toEntity(parentMember, NotificationContent.REPLY_ON_MY_COMMENT));
                    }

                    // parent member와 article author가 불일치한다면 article author에게도 알림
                    if (!author.getId().equals(sender.getId()) && !parentMember.getId().equals(author.getId())) {
                        notifications.add(newNotificationServiceDto.toEntity(author, NotificationContent.COMMENT_ON_MY_ARTICLE));
                    }

                } else if(!author.getId().equals(sender.getId())) {  // parent member도 존재하지 않는다면 article author에게만 알림
                    notifications.add(newNotificationServiceDto.toEntity(author, NotificationContent.COMMENT_ON_MY_ARTICLE));
                }
            }


            notificationRepository.saveAll(notifications);
        }
    }

    public boolean isNotSender(Member sender, Member receiver){
        return sender != receiver;
    }

    @Transactional
    public Integer countNewNotifications (Member member) {
        return notificationRepository.countNewNotification(member);
    }

    @Transactional
    public ResponseCookie newNotificationsCountCookie(Integer count) {
        if(count > 0) {
            return ResponseCookie.from("count_new_notifications", count.toString())
                    .path("/")
                    .domain(".code-in.site")
                    .build();
        } else {
            return null;
        }
    }
    
    @Transactional
    public NotificationListResponseDto getNotifications(GetNotificationsServiceDto getNotificationsServiceDto) {
        Member member = memberRepository.findByAccessToken(getNotificationsServiceDto.getAccessToken());
        if(member != null) {
            if(getNotificationsServiceDto.getLastNotificationId() == Long.MAX_VALUE) {    // 알림버튼을 클릭하여 처음 열람한 경우
                checkNotifications(member);     // checked가 false인 모든 알림을 true로 변경
            }
            return notificationRepository.getNotificationList(getNotificationsServiceDto, member);
        } else {
            throw new MemberNotExistsException();
        }
    }

    @Transactional
    public void checkNotifications(Member member){
        List<Notification> notifications = notificationRepository.findNotCheckedByReceiver(member);
        if(!notifications.isEmpty()) {
            notifications.forEach(Notification::setChecked);
        }
    }

    @Transactional
    public ResponseCookie removeNewNotificationsCountCookie() {
        return ResponseCookie.from("count_new_notifications", "0")
                .path("/")
                .maxAge(0)
                .domain(".code-in.site")
                .build();
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
