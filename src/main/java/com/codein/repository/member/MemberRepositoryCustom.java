package com.codein.repository.member;

import com.codein.domain.member.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    Member findByEmail(String email);

    Member findByPhone(String phone);

    Member findByNickname(String nickname);

    Member findByAccessToken(String accessToken);

    Member findByRefreshToken(String refreshToken);

    boolean existsByNickname(String nickname);

    List<Member> findByFollowing(Member sender);


}
