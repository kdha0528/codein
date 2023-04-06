package com.codein.requestservicedto;

import com.codein.domain.Category;
import com.codein.domain.Member;
import com.codein.domain.Post;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class PostingServiceDto {
    @NotBlank(message = "카테고리를 입력해주세요.")
    private final String category;

    @NotBlank(message = "제목을 입력해주세요.")
    @Size(min = 1, max = 50)
    private final String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Min(1)
    private final String content;

    @Builder
    public PostingServiceDto(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

    public Post toEntity(Member member) {
        return Post.builder()
                .member(member)
                .category(Category.valueOf(this.category))
                .title(this.title)
                .content(this.content)
                .build();

    }
}
