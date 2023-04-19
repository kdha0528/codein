package com.codein.repository.member;

import com.codein.domain.member.Member;
import com.codein.requestdto.PageSizeDto;
import com.codein.responsedto.MemberListResponseDto;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberListResponseDto> getMemberResponseList(PageSizeDto pageSizeDto);

    Member findByEmail(String email);

    Member findByPhone(String phone);

    Member findByNickname(String nickname);

    Member findByAccessToken(String accessToken);

    Member findByRefreshToken(String refreshToken);


}
