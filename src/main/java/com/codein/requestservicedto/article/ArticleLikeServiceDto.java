package com.codein.requestservicedto.article;

import com.codein.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
@Getter
public class ArticleLikeServiceDto {
        private final Long articleId;
        private final Member member;

        @Builder
        public ArticleLikeServiceDto(Long articleId, Member member) {
            this.articleId = articleId;
            this.member = member;
        }
}
