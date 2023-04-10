package com.codein.repository.article;

import com.codein.domain.article.Article;
import com.codein.domain.member.Member;

import java.util.List;

public interface ArticleRepositoryCustom {

    List<Article> findByMember(Member member);

}
