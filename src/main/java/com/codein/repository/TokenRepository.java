package com.codein.repository;

import com.codein.domain.auth.Token;
import com.codein.domain.member.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenRepository extends CrudRepository<Token, Long> {
    Optional<Token> findByAccessToken(String accessToken); //accessToken으로 값을 못찾을 수 있기 때문에 optional 사용

    Optional<Token> findByRefreshToken(String refreshToken);

    Optional<Token> findByMember(Member member);

}
