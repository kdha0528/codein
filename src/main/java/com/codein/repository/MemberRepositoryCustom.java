package com.codein.repository;

import com.codein.domain.Member;
import com.codein.requestdto.PageSizeDto;
import com.codein.responsedto.MemberResponseDto;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberResponseDto> getMemberResponseList(PageSizeDto pageSizeDto);

    Member findByEmail(String email);

    Member findByPhone(String email);

    Member findByAccessToken(String accessToken);

}
