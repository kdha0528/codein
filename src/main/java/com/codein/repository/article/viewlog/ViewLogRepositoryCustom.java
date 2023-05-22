package com.codein.repository.article.viewlog;

import com.codein.domain.article.ViewLog;
import com.codein.domain.member.Member;
import com.codein.requestservicedto.article.GetArticleServiceDto;

public interface ViewLogRepositoryCustom {

    ViewLog findByGetArticleServiceDto(GetArticleServiceDto getArticleServiceDto);
}
