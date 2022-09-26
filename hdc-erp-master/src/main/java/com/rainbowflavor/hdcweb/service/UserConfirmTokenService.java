package com.rainbowflavor.hdcweb.service;

import com.rainbowflavor.hdcweb.domain.UserConfirmToken;
import com.rainbowflavor.hdcweb.exception.request.BadRequestException;
import com.rainbowflavor.hdcweb.repository.JpaUserConfirmTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserConfirmTokenService {
    private final static String RECEIVE_EMAIL = "degeh91710@nnacell.com";

    private final JpaUserConfirmTokenRepository repository;
    private final EmailSenderService emailSenderService;

    public String createEmailConfirmToken(Long userId) {
        UserConfirmToken emailConfirmationToken = UserConfirmToken.createEmailConfirmToken(userId);
        repository.save(emailConfirmationToken);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(RECEIVE_EMAIL);
        mailMessage.setSubject("회원가입 이메일 인증");
        mailMessage.setText("http://localhost:8080/confirm-signup?token="+emailConfirmationToken.getId());
        emailSenderService.sendEmail(mailMessage);

        return emailConfirmationToken.getId();
    }

    public UserConfirmToken findByIdAndExpirationDateAfterAndExpired(String confirmationTokenId){
        Optional<UserConfirmToken> confirmationToken = repository.findByIdAndExpirationDateTimeAfterAndExpired(confirmationTokenId, LocalDateTime.now(),false);
        return confirmationToken.orElseThrow(()-> new BadRequestException("TOKEN_NOT_FOUND"));
    }
}
