package com.rainbowflavor.hdcweb.handler.security.authentication;

import com.rainbowflavor.hdcweb.exception.security.authentication.EmailVerifyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final String authenticationFailureUrl = "/mail-auth";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String errorMsg;
        if(exception instanceof EmailVerifyException){
            errorMsg = "이메일 인증을 완료해주세요.";
        }else if(exception instanceof BadCredentialsException){
            errorMsg = "아이디나 비밀번호가 맞지 않습니다. 다시 확인해 주세요.";
        }else if(exception instanceof DisabledException) {
            errorMsg = "계정이 비활성화 되었습니다. 관리자에게 문의하세요.";
        }else if(exception instanceof LockedException){
            errorMsg = "이메일이 인증되지 않았습니다. 이메일을 확인해 주세요.";
        }else{
            errorMsg = "알수없는 이유로 로그인에 실패하였습니다.";
        }

        request.getRequestDispatcher(authenticationFailureUrl).forward(request, response);
        request.setAttribute("errorMsg", errorMsg);
    }
}
