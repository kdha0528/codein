package com.codein.controller;

import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.member.Role;
import com.codein.requestservicedto.notification.ClickNotificationServiceDto;
import com.codein.requestservicedto.notification.GetNotificationsServiceDto;
import com.codein.responsedto.notification.NotificationListResponseDto;
import com.codein.service.NotificationService;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @MySecured(role = Role.MEMBER)
    @GetMapping({"/notifications"})
    public NotificationListResponseDto getNotifications(@CookieValue(value = "accesstoken") Cookie cookie,
                                                        @RequestParam(value = "lastId", required = false) Long lastId) {
        GetNotificationsServiceDto getNotificationsServiceDto = GetNotificationsServiceDto.builder()
                .lastNotificationId(lastId)
                .accessToken(cookie.getValue())
                .build();
        return notificationService.getNotifications(getNotificationsServiceDto);
    }

    @MySecured(role = Role.MEMBER)
    @PostMapping({"/notifications/{id}"})
    public void clickNotification(@CookieValue(value = "accesstoken") Cookie cookie,
                                  @PathVariable(value = "id") Long id) {
        ClickNotificationServiceDto clickNotificationServiceDto = ClickNotificationServiceDto.builder()
                .accessToken(cookie.getValue())
                .notificationId(id)
                .build();
        notificationService.clickNotification(clickNotificationServiceDto);

    }

}
