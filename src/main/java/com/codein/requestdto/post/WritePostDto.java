package com.codein.requestdto.post;

import com.codein.requestservicedto.post.WritePostServiceDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class WritePostDto {

    @NotBlank(message = "카테고리를 입력해주세요.")
    private final String category;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 50)
    private final String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 1)
    private final String content;

    @Builder
    public WritePostDto(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

    public WritePostServiceDto toWritePostServiceDto() {
        return WritePostServiceDto.builder()
                .category(this.category)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
