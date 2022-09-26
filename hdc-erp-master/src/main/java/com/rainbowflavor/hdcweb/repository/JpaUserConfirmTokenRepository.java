package com.rainbowflavor.hdcweb.repository;

import com.rainbowflavor.hdcweb.domain.UserConfirmToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaUserConfirmTokenRepository extends JpaRepository<UserConfirmToken, String> {
    Optional<UserConfirmToken> findByIdAndExpirationDateTimeAfterAndExpired(String userConfirmTokenId, LocalDateTime now, boolean expired);
}
