package com.codein.requestdto.post;


import com.codein.requestservicedto.article.EditArticleServiceDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EditArticleDto {

    @NotBlank(message = "카테고리를 입력해주세요.")
    private final String category;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 50)
    private final String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 1)
    private final String content;

    @Builder
    public EditArticleDto(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

    public EditArticleServiceDto toEditPostServiceDto() {

        return EditArticleServiceDto.builder()
                .category(category)
                .title(title)
                .content(content)
                .build();

    }
}
