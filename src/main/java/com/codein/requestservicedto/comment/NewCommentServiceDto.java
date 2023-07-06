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
    private final Long parentId;
    private final Long targetId;
    private final String accessToken;

    @Builder
    public NewCommentServiceDto(Long articleId, String content, Long parentId, Long targetId, String accessToken) {
        this.content = content;
        this.articleId = articleId;
        this.parentId = parentId;
        this.targetId = targetId;
        this.accessToken = accessToken;
    }

    public String hasTarget(){
        int index = this.content.indexOf("@");
        if (index == 0) return this.content.substring(index).split(" ")[0];
        else return null;
    }

}
