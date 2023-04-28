package com.codein.repository;

import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokensRepository extends CrudRepository<Tokens, Long> {
    Optional<Tokens> findByAccessToken(String accessToken); //accessToken으로 값을 못찾을 수 있기 때문에 optional 사용

    Optional<Tokens> findByRefreshToken(String refreshToken);

    Optional<Tokens> findByMember(Member member);

}
