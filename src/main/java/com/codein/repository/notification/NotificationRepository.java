package com.codein.repository.notification;

import com.codein.domain.member.Member;
import com.codein.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {
    List<Notification> findAllBySender(Member member);
}
