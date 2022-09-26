package com.rainbowflavor.hdcweb.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class UserConfirmToken {

    private static final long EMAIL_TOKEN_TTL = 10L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(length = 36)
    private String id;

    @Column
    private LocalDateTime expirationDateTime;

    @Column
    private boolean expired;

    @Column
    private Long userId;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createDateTime;

    @LastModifiedDate
    private LocalDateTime lastModifiedDateTime;

    public static UserConfirmToken createEmailConfirmToken(Long userId) {
        UserConfirmToken userConfirmToken = new UserConfirmToken();
        userConfirmToken.expirationDateTime = LocalDateTime.now().plusMinutes(EMAIL_TOKEN_TTL);
        userConfirmToken.userId = userId;
        return userConfirmToken;
    }

    public void useToken() {
        expired = true;
    }
}
