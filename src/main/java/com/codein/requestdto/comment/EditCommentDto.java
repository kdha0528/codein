package com.codein.requestdto.comment;

import com.codein.requestservicedto.comment.EditCommentServiceDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class EditCommentDto {

    private final Long id;

    @NotBlank(message = "댓글을 1자 이상 적어주세요.")
    @Size(min = 1, max = 500, message = "댓글은 최대 500글자까지 입력가능합니다.")
    private final String content;

    @Builder
    public EditCommentDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }

    public EditCommentServiceDto toEditCommentServiceDto(String accessToken){

        return EditCommentServiceDto.builder()
                .id(this.id)
                .content(this.content)
                .accessToken(accessToken)
                .build();
    }
}
