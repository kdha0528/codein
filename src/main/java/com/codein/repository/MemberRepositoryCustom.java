package com.codein.repository;

import com.codein.domain.Member;
import com.codein.requestdto.PageSizeDto;
import com.codein.responsedto.LoginResponseDto;

import java.util.List;

public interface MemberRepositoryCustom {

    List<LoginResponseDto> getMemberResponseList(PageSizeDto pageSizeDto);

    Member findByEmail(String email);

    Member findByPhone(String phone);

    Member findByNickname(String nickname);

    Member findByAccessToken(String accessToken);

}
