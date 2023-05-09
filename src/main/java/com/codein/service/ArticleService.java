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
import com.codein.repository.member.MemberRepositoryCustom;
import com.codein.requestdto.article.GetArticlesDto;
import com.codein.requestservicedto.article.EditArticleServiceDto;
import com.codein.requestservicedto.article.NewArticleServiceDto;
import com.codein.responsedto.ArticleListResponseDto;
import com.codein.responsedto.ArticleResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final TokensRepository tokensRepository;
    private final MemberRepositoryCustom memberRepository;

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
    public ArticleListResponseDto getArticleList(GetArticlesDto getArticlesDto, Category category) {
        return ArticleListResponseDto.builder()
                .articleList(articleRepository.getArticleList(getArticlesDto, category))
                .maxPage(articleRepository.getMaxPage(getArticlesDto, category))
                .build();
    }
    @Transactional
    public void createDummies(Member member) {
        if(member == null) throw new MemberNotLoginException();
        if(articleRepository.findByMember(member).isEmpty()){
            Random random = new Random();
            for(int i = 0; i < 600; i ++) {
                int categoryFlag = random.nextInt(3);
                Category category =  Category.COMMUNITY;
                switch(categoryFlag){
                    case 1:
                        category = Category.QUESTION;
                        break;
                    case 2:
                        category = Category.INFORMATION;
                    default:
                        break;
                }

                Article article = Article.builder()
                        .member(member)
                        .category(category)
                        .title("Title No."+i)
                        .content("카테고리는 " + category.getName() + "입니다.")
                        .viewNum(random.nextInt(100))
                        .commentNum(random.nextInt(100))
                        .likeNum(random.nextInt(100))
                        .build();
                articleRepository.save(article);
            }
        }
    }

}
