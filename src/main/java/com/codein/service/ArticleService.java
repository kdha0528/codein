package com.codein.service;

import com.codein.domain.article.Article;
import com.codein.domain.article.ArticleEditor;
import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import com.codein.error.exception.article.ArticlePostNotExistsException;
import com.codein.error.exception.member.MemberNotLoginException;
import com.codein.repository.TokensRepository;
import com.codein.repository.article.ArticleRepository;
import com.codein.requestservicedto.article.EditArticleServiceDto;
import com.codein.requestservicedto.article.NewArticleServiceDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TokensRepository tokensRepository;

    @Transactional
    public void newArticle(NewArticleServiceDto newArticleServiceDto, String accesstoken) {
        Tokens tokens = tokensRepository.findByAccessToken(accesstoken)
                .orElseThrow(MemberNotLoginException::new);
        Member member = tokens.getMember();
        articleRepository.save(newArticleServiceDto.toEntity(member));
    }

    @Transactional
    public void editArticle(Long articleId, EditArticleServiceDto editArticleServiceDto) {
        ArticleEditor articleEditor = ArticleEditor.builder()
                .category(editArticleServiceDto.getCategory())
                .title(editArticleServiceDto.getTitle())
                .content(editArticleServiceDto.getContent())
                .build();

        Article article = articleRepository.findById(articleId)
                .orElseThrow(ArticlePostNotExistsException::new);

        article.edit(articleEditor);
    }
}
