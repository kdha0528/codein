package com.codein.repository.member;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codein.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    private final PasswordEncoder passwordEncoder;


    public Member findByEmail(String email) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.email.eq(email))
                .fetchOne();
    }

    public Member findByPhone(String phone) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.phone.eq(phone))
                .fetchOne();
    }

    public Member findByNickname(String nickname) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.nickname.eq(nickname))
                .fetchOne();
    }

    public Member findByAccessToken(String accessToken) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.tokens.any().accessToken.eq(accessToken))
                .fetchOne();
    }

    public Member findByRefreshToken(String refreshToken) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.tokens.any().refreshToken.eq(refreshToken))
                .fetchOne();
    }
}
