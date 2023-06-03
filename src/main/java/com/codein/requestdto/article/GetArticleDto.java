package com.codein.requestdto.article;

import com.codein.requestservicedto.article.GetArticleServiceDto;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
public class GetArticleDto {

    private final Long id;
    private final Integer cpage;

    @Builder
    public GetArticleDto(Long id, Integer cpage) {
        this.id = id;
        this.cpage = Objects.requireNonNullElse(cpage, 1);
    }

    public GetArticleServiceDto toGetArticleServiceDto(String clientIp){
        return GetArticleServiceDto.builder()
                .commentPage(this.cpage)
                .articleId(this.id)
                .clientIp(clientIp)
                .build();
    }
}
