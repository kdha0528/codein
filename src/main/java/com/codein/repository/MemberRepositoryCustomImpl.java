package com.codein.repository;

import com.codein.domain.Member;
import com.codein.request.PageSize;
import com.codein.response.MemberResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

import static com.codein.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {


    private final JPAQueryFactory jpaQueryFactory;

    public List<MemberResponse> getMemberResponseList(PageSize pageSize) {
        List<Member> memberList = jpaQueryFactory.selectFrom(member)
                .limit(pageSize.getSize())
                .offset(pageSize.getOffset())
                .orderBy(member.id.desc())
                .stream()
                .toList();

        return memberList.stream()
                .map(Member::changeMemberResponse)
                .collect(Collectors.toList());
    }

}
