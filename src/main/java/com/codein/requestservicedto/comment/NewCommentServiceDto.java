package com.codein.requestservicedto.comment;

import com.codein.domain.article.Article;
import com.codein.domain.comment.Comment;
import com.codein.domain.member.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
public class NewCommentServiceDto {

    private final Long articleId;
    private final String content;
    private final Long targetId;
    private final String accessToken;

    @Builder
    public NewCommentServiceDto(Long articleId, String content, Long targetId, String accessToken) {
        this.articleId = articleId;
        this.content = content;
        this.targetId = targetId;
        this.accessToken = accessToken;
    }

    public Comment toEntity(Member member, Article article, Comment target) {
        return Comment.builder()
                .member(member)
                .article(article)
                .target(target)
                .content(this.content)
                .build();
    }
}
