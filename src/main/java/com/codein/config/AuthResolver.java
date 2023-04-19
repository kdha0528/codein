package com.codein.config;

import com.codein.config.data.MemberSession;
import com.codein.domain.auth.Token;
import com.codein.error.exception.auth.UnauthorizedException;
import com.codein.repository.TokenRepository;
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

    private final TokenRepository tokenRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // httpRequest 존재 여부 확인
        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);

        if (httpServletRequest == null) {
            log.error("httpServletRequest is null");
            throw new NullPointerException();
        }

        Cookie[] cookies = httpServletRequest.getCookies();

        // cookies에서 session이 있는 쿠키를 반환하는 메소드
        Function<Cookie[], Cookie> validateSessionCookie = cookieList -> {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals("accesstoken")) {
                    return cookie;
                }
            }
            throw new NullPointerException();
        };

        // validateSessionCookie 실행 후 반환된 cookie의 value를 accessToken에 저장
        String accessToken = validateSessionCookie.apply(cookies).getValue();

        // accessToken이 null인지 확인
        if (accessToken == null || accessToken.equals("")) {
            throw new UnauthorizedException();
        }

        // accessToken이 유효한지 확인
        Token token = tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(UnauthorizedException::new);

        // 해당 토큰을 가지고 있는 유저의 아이디를 MemberSession 형태로 반환
        return new MemberSession(token.getMember().getId());
    }
}
