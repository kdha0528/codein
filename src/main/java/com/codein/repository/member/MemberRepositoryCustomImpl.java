package com.codein.repository.member;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.member.Member;
import com.codein.requestdto.PageSizeDto;
import com.codein.responsedto.MemberListResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.codein.domain.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    private final PasswordEncoder passwordEncoder;


    public List<MemberListResponseDto> getMemberResponseList(PageSizeDto pageSizeDto) {
        List<Member> memberList = jpaQueryFactory.selectFrom(member)
                .limit(pageSizeDto.getSize())
                .offset(pageSizeDto.getOffset())
                .orderBy(member.id.desc())
                .fetch();

        return memberList.stream().map(Member::toMemberListResponseDto)
                .collect(Collectors.toList());
    }

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
