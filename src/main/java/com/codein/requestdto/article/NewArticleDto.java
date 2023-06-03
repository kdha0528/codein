package com.codein.requestdto.article;

import com.codein.requestservicedto.article.NewArticleServiceDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NewArticleDto {

    @NotBlank(message = "카테고리를 입력해주세요.")
    private final String category;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(max = 50, message = "제목은 50글자 이하로 작성해주세요.")
    private final String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 10000, message = "최대 글자수를 초과하였습니다.")
    private final String content;

    @Builder
    public NewArticleDto(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

    public NewArticleServiceDto toNewArticleServiceDto() {
        return NewArticleServiceDto.builder()
                .category(this.category)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
