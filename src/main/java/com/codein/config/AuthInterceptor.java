package com.codein.config;

import com.codein.config.SecurityConfig.MySecured;
import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import com.codein.domain.member.Role;
import com.codein.error.exception.auth.AccessTokenNullException;
import com.codein.error.exception.auth.InvalidAccessTokenException;
import com.codein.error.exception.auth.UnauthorizedException;
import com.codein.repository.TokensRepository;
import com.codein.repository.member.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import java.util.function.Function;

@RequiredArgsConstructor
@Slf4j
public class AuthInterceptor implements HandlerInterceptor {

    private final TokensRepository tokensRepository;
    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 형 변환하기
        if (handler instanceof ResourceHttpRequestHandler) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // @MySecured 받아오기
        MySecured mySecured = handlerMethod.getMethodAnnotation(MySecured.class);

        // method에 @MySecured가 없는 경우, 즉 인증이 필요 없는 요청
        if (mySecured == null) {
            return true;
        }

        // cookies에서 session이 있는 쿠키를 반환하는 메소드
        Cookie[] cookies = request.getCookies();
        Function<Cookie[], Cookie> validateAccessCookie = cookieList -> {
            for (Cookie cookie : cookieList) {
                if (cookie.getName().equals("accesstoken")) {
                    return cookie;
                }
            }
            throw new AccessTokenNullException();
        };

        // validateSessionCookie 실행 후 반환된 cookie의 value를 accessToken에 저장
        String accessToken = validateAccessCookie.apply(cookies).getValue();
        System.out.println("accesstoken = " + accessToken);

        // accessToken이 존재하면 유효한 세션인지 확인
        Tokens tokens = tokensRepository.findByAccessToken(accessToken)
                .orElseThrow(InvalidAccessTokenException::new);

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
