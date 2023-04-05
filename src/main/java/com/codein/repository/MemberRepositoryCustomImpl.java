package com.codein.repository;

import com.codein.crypto.PasswordEncoder;
import com.codein.domain.Member;
import com.codein.requestdto.PageSizeDto;
import com.codein.responsedto.LoginResponseDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.codein.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final EntityManager em;
    private final JPAQueryFactory jpaQueryFactory;

    private final PasswordEncoder passwordEncoder;

    public List<LoginResponseDto> getMemberResponseList(PageSizeDto pageSizeDto) {
        List<Member> memberList = jpaQueryFactory.selectFrom(member)
                .limit(pageSizeDto.getSize())
                .offset(pageSizeDto.getOffset())
                .orderBy(member.id.desc())
                .fetch();

        return memberList.stream().map(Member::changeMemberResponse)
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
                .where(member.sessions.any().accessToken.eq(accessToken))
                .fetchOne();
    }

}
