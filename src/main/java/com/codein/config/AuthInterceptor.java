package com.codein.config;

import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.member.Member;
import com.codein.domain.member.Role;
import com.codein.error.exception.auth.AccessTokenNullException;
import com.codein.error.exception.auth.InvalidAccessTokenException;
import com.codein.error.exception.auth.RefreshTokenNullException;
import com.codein.error.exception.auth.UnauthorizedException;
import com.codein.repository.tokens.TokensRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.service.NotificationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.Objects;


@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final TokensRepository tokensRepository;
    private final MemberRepository memberRepository;
    private final NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        String accessToken = null;
        Integer beforeNotificationCount = null;

        response.setContentType("application/json");

        // 정적 리소스
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // refresh token이 있고, access token이 없는 경우 access token 재발급
        if(!request.getRequestURI().equals("/refreshtoken") && cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accesstoken")) accessToken = cookie.getValue();
                if (cookie.getName().equals("refreshtoken")) refreshToken = cookie.getValue();
                if (cookie.getName().equals("count_new_notifications")) {
                    beforeNotificationCount = Integer.parseInt(cookie.getValue());
                }
            }
            System.out.println("refresh token = " + refreshToken);
            System.out.println("access token = " + accessToken);
            if(refreshToken != null && accessToken == null) {
                throw new AccessTokenNullException();
            }
        }

        // 로그인 되어있다면 새 알림 개수 cookie로 전달
        if(refreshToken != null) {
            // accessToken이 존재하면 유효한 세션인지 확인
            tokensRepository.findByAccessToken(accessToken).orElseThrow(InvalidAccessTokenException::new);

            Member member = memberRepository.findByAccessToken(accessToken);
            Integer newNotificationCount = notificationService.countNewNotifications(member);

            // 새 알림이 있는 경우
            if(newNotificationCount != 0){
                if(beforeNotificationCount != null) {   // 이전 새 알림 개수 쿠키가 있었던 경우
                    if (!Objects.equals(beforeNotificationCount, newNotificationCount)) {   // 이전 새 알림 개수와 현재 새 알림 개수가 다른 경우 쿠키 업데이트
                        response.addHeader(HttpHeaders.SET_COOKIE, notificationService.newNotificationsCountCookie(newNotificationCount).toString());
                    }
                } else {    // 이전 새 알림 개수가 없다면 새 알림 개수 쿠키 생성
                    response.addHeader(HttpHeaders.SET_COOKIE, notificationService.newNotificationsCountCookie(newNotificationCount).toString());
                }
            }
        }

        // @MySecured 받아오기
        MySecured mySecured = handlerMethod.getMethodAnnotation(MySecured.class);

        // method에 @MySecured가 없는 경우, 즉 인증이 필요 없는 요청
        if (mySecured == null) {
            return true;
        }

        // 로그인이 안되어 있을 경우 에러
        if(refreshToken == null){
            throw new RefreshTokenNullException();
        }

        // 유효한 세션이라면 해당 유저 가져오기
        Member member = memberRepository.findByAccessToken(accessToken);

        // 접근하는 컨트롤러의 role 확인
        String role = mySecured.role().toString();

        // 접근하는 유저의 role 확인
        if (role != null) {
            // admin일 경우
            if (role.equals("ADMIN")) {
                return member.getRole() == Role.ADMIN;
            }
            // member일 경우
            else if (role.equals("MEMBER")) {
                if (member.getRole() == Role.ADMIN) {
                    return true;
                } else if (member.getRole() == Role.MEMBER) {
                    return true;
                } else {
                    throw new UnauthorizedException();
                }
            }
            // none일 경우
            else {
                throw new UnauthorizedException();
            }
        }

        throw new UnauthorizedException();
    }
}
