package com.codein.repository.member;

import com.codein.domain.member.Member;

public interface MemberRepositoryCustom {

    Member findByEmail(String email);

    Member findByPhone(String phone);

    Member findByNickname(String nickname);

    Member findByAccessToken(String accessToken);

    Member findByRefreshToken(String refreshToken);


}
