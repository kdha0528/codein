package com.codein.controller;

import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.member.Role;
import com.codein.requestservicedto.notification.ClickNotificationServiceDto;
import com.codein.requestservicedto.notification.GetNotificationsServiceDto;
import com.codein.responsedto.notification.NotificationListResponseDto;
import com.codein.service.NotificationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @MySecured(role = Role.MEMBER)
    @GetMapping({"/notifications"})
    public NotificationListResponseDto getNotifications(@CookieValue(value = "accesstoken") Cookie cookie,
                                                        @RequestParam(value = "lastId", required = false) Long lastId,
                                                        HttpServletResponse response) {
        GetNotificationsServiceDto getNotificationsServiceDto = GetNotificationsServiceDto.builder()
                .lastNotificationId(lastId)
                .accessToken(cookie.getValue())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, notificationService.removeNewNotificationsCountCookie().toString());
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
