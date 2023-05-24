package com.codein.repository.article;

import com.codein.domain.article.Article;
import com.codein.domain.member.Member;
import com.codein.requestservicedto.article.GetActivitiesServiceDto;
import com.codein.requestservicedto.article.GetArticlesServiceDto;
import com.codein.responsedto.article.ActivityListResponseDto;
import com.codein.responsedto.article.ArticleListResponseDto;

import java.util.List;

public interface ArticleRepositoryCustom {

    List<Article> findByMember(Member member);
    Article findByMemberLatest(Member member);
    ArticleListResponseDto getArticleList(GetArticlesServiceDto getArticlesServiceDto);
    ActivityListResponseDto getActivityListResponseDto(GetActivitiesServiceDto getActivitiesServiceDto, Member member);

}
