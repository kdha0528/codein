package com.codein.requestdto.article;

import com.codein.domain.member.Member;
import com.codein.repository.member.MemberRepository;
import com.codein.requestservicedto.article.EditArticleServiceDto;
import jakarta.servlet.http.Cookie;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @Size(min = 5, max = 50, message = "제목을 5~50글자로 맞춰주세요.")
    private final String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(min = 5, message = "내용을 5글자 이상 입력해주세요.")
    private final String content;

    @Builder
    public EditArticleDto(String category, String title, String content) {
        this.category = category;
        this.title = title;
        this.content = content;
    }

    public EditArticleServiceDto toEditArticleServiceDto(Long id, String accessToken) {
        return EditArticleServiceDto.builder()
                .id(id)
                .accessToken(accessToken)
                .category(this.category)
                .title(this.title)
                .content(this.content)
                .build();
    }
}
