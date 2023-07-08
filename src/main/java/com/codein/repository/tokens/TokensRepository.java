package com.codein.repository.tokens;

import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import com.codein.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokensRepository extends JpaRepository<Tokens, Long>, TokensRepositoryCustom {
    Optional<Tokens> findByAccessToken(String accessToken); //accessToken으로 값을 못찾을 수 있기 때문에 optional 사용

    Optional<Tokens> findByMember(Member member);

}
