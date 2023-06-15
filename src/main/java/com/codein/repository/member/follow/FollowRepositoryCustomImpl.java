package com.codein.repository.member.follow;

import com.codein.domain.member.Member;
import com.codein.domain.member.follow.Follow;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codein.domain.member.follow.QFollow.follow;

@Repository
@RequiredArgsConstructor
public class FollowRepositoryCustomImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Follow findByMembers(Member follower, Member following) {
        return jpaQueryFactory.selectFrom(follow)
                .where(follow.follower.eq(follower), follow.following.eq(following))
                .fetchOne();
    }
}
