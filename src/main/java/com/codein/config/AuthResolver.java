package com.codein.config;

import com.codein.config.data.MemberSession;
import com.codein.domain.Session;
import com.codein.exception.Unauthorized;
import com.codein.repository.SessionRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.function.Function;

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        /*HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        // request 존재 여부 확인
        if (httpServletRequest == null) {
            log.error("httpServletRequest is null");
            throw new Unauthorized();
        }

        // request가 존재한다면 그 안에 cookie가 있는지 확인
        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies.length == 0) {
            log.error("쿠키가 없음");
            throw new Unauthorized();
        }*/

        // httpRequest 존재 여부 확인
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        if (httpServletRequest == null) {
            log.error("httpServletRequest is null");
            throw new Unauthorized();
        }

        Cookie[] cookies = httpServletRequest.getCookies();

        // cookies에서 session이 있는 쿠키를 반환하는 메소드
        Function<Cookie[], Cookie> validateSessionCookie = cookieList -> {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals("SESSION")) {
                    return cookie;
                }
            }
            throw new Unauthorized();
        };

        // validateSessionCookie 실행 후 반환된 cookie의 value를 accessToken에 저장
        String accessToken = validateSessionCookie.apply(cookies).getValue();

        // accessToken이 null인지 확인
        if (accessToken == null || accessToken.equals("")) {
            throw new Unauthorized();
        }

        // accessToken이 유효한지 확인
        Session session = sessionRepository.findByAccessToken(accessToken)
                .orElseThrow(Unauthorized::new);

        // 해당 토큰을 가지고 있는 유저의 아이디를 MemberSession 형태로 반환
        return new MemberSession(session.getMember().getId());
    }
}
