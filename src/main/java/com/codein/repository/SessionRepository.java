package com.codein.repository;

import com.codein.domain.Session;
import com.codein.domain.member.Member;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, Long> {
    Optional<Session> findByAccessToken(String accessToken); //accessToken으로 값을 못찾을 수 있기 때문에 optional 사용

    Optional<Session> findByMember(Member member);

}
