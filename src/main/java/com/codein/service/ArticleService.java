package com.codein.service;

import com.codein.domain.article.Article;
import com.codein.domain.article.ArticleEditor;
import com.codein.domain.auth.Token;
import com.codein.domain.member.Member;
import com.codein.error.exception.article.ArticlePostNotExistsException;
import com.codein.error.exception.member.MemberNotLoginException;
import com.codein.repository.TokenRepository;
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
    private final TokenRepository tokenRepository;

    @Transactional
    public void newArticle(NewArticleServiceDto newArticleServiceDto, String accesstoken) {
        Token token = tokenRepository.findByAccessToken(accesstoken)
                .orElseThrow(MemberNotLoginException::new);
        Member member = token.getMember();
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
