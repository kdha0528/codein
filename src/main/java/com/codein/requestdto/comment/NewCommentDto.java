package com.codein.requestdto.comment;

import com.codein.requestservicedto.comment.NewCommentServiceDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class NewCommentDto {

    @NotBlank(message = "댓글을 입력해주세요.")
    @Size(min = 1, max = 3000, message = "댓글은 최대 3000글자까지 입력가능합니다.")
    private final String content;

    private final Long parentId;
    private final Long targetId;

    @Builder
    public NewCommentDto(String content,Long parentId, Long targetId) {
        this.content = content;
        this.parentId = parentId;
        this.targetId = targetId;
    }

    public NewCommentServiceDto toNewCommentServiceDto(String accessToken, Long articleId) {
        return NewCommentServiceDto.builder()
                .articleId(articleId)
                .accessToken(accessToken)
                .parentId(this.parentId)
                .targetId(this.targetId)
                .content(this.content)
                .build();
    }
}
