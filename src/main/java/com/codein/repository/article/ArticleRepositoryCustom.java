package com.codein.repository.article;

import com.codein.domain.article.Article;
import com.codein.domain.article.Category;
import com.codein.domain.member.Member;
import com.codein.requestdto.article.GetActivityDto;
import com.codein.requestdto.article.GetArticlesDto;
import com.codein.responsedto.ActivityListResponseDto;
import com.codein.responsedto.ArticleListResponseDto;

import java.util.List;

public interface ArticleRepositoryCustom {

    List<Article> findByMember(Member member);
    ArticleListResponseDto getArticleList(GetArticlesDto getArticlesDto);
    ActivityListResponseDto getActivityListResponseDto(GetActivityDto getActivityDto, Member member);

}
