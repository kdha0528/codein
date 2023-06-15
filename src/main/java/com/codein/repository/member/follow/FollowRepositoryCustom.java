package com.codein.repository.member.follow;

import com.codein.domain.member.Member;
import com.codein.domain.member.follow.Follow;

public interface FollowRepositoryCustom {

    Follow findByMembers(Member follower, Member following);
}
