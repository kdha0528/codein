package com.codein.repository;

import com.codein.domain.Member;
import com.codein.request.PageSize;
import com.codein.response.MemberResponse;

import java.util.List;

public interface MemberRepositoryCustom {

    List<MemberResponse> getMemberResponseList(PageSize pageSize);
}
