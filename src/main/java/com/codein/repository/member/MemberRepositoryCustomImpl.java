package com.codein.repository.member;

import com.codein.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codein.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;



    public Member findByEmail(String email) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.email.eq(email),
                        member.deleted.isFalse())
                .fetchOne();
    }

    public Member findByPhone(String phone) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.phone.eq(phone),
                        member.deleted.isFalse())
                .fetchOne();
    }

    public Member findByNickname(String nickname) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.nickname.eq(nickname),
                        member.deleted.isFalse())
                .fetchOne();
    }

    public Member findByAccessToken(String accessToken) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.tokens.any().accessToken.eq(accessToken),
                        member.deleted.isFalse())
                .fetchOne();
    }

    public Member findByRefreshToken(String refreshToken) {
        return jpaQueryFactory.selectFrom(member)
                .where(member.tokens.any().refreshToken.eq(refreshToken),
                        member.deleted.isFalse())
                .fetchOne();
    }

    @Override
    public boolean existsByNickname(String nickname) {
        Member result = jpaQueryFactory.selectFrom(member)
                .where(member.nickname.eq(nickname),
                        member.deleted.isFalse())
                .fetchFirst();
        return result != null;
    }
}
