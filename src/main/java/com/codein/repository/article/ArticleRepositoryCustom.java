package com.codein.repository.article;

import com.codein.domain.article.Article;
import com.codein.domain.article.Category;
import com.codein.domain.member.Member;
import com.codein.requestdto.article.GetArticlesDto;
import com.codein.responsedto.ArticleResponseDto;

import java.util.List;

public interface ArticleRepositoryCustom {

    List<Article> findByMember(Member member);
    List<ArticleResponseDto> getArticleList(GetArticlesDto getArticlesDto, Category category);
    Integer getMaxPage(GetArticlesDto getArticlesDto, Category category);

}
