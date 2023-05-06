package com.codein.service;

import com.codein.domain.article.Article;
import com.codein.domain.article.ArticleEditor;
import com.codein.domain.article.Category;
import com.codein.domain.auth.Tokens;
import com.codein.domain.member.Member;
import com.codein.error.exception.article.ArticlePostNotExistsException;
import com.codein.error.exception.member.MemberNotLoginException;
import com.codein.repository.TokensRepository;
import com.codein.repository.article.ArticleRepository;
import com.codein.requestdto.GetArticlesDto;
import com.codein.requestservicedto.article.EditArticleServiceDto;
import com.codein.requestservicedto.article.NewArticleServiceDto;
import com.codein.responsedto.ArticleListResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public void editArticle(EditArticleServiceDto editArticleServiceDto) {
        Article article = articleRepository.findById(editArticleServiceDto.getId())
                .orElseThrow(ArticlePostNotExistsException::new);

        ArticleEditor articleEditor = ArticleEditor.builder()
                .category(editArticleServiceDto.getCategory())
                .title(editArticleServiceDto.getTitle())
                .content(editArticleServiceDto.getContent())
                .build();

        article.edit(articleEditor);
    }

    @Transactional
    public List<ArticleListResponseDto> getArticleList(GetArticlesDto getArticlesDto, Category category) {
        return articleRepository.getArticleList(getArticlesDto, category);
    }
}
