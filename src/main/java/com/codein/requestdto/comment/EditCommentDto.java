package com.codein.requestdto.comment;

import com.codein.requestservicedto.comment.EditCommentServiceDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class EditCommentDto {

    @NotBlank(message = "댓글을 1자 이상 적어주세요.")
    @Size(min = 1, max = 500, message = "댓글은 최대 500글자까지 입력가능합니다.")
    private String content;

    @Builder
    public EditCommentDto(String content) {
        this.content = content;
    }

    public EditCommentServiceDto toEditCommentServiceDto(String accessToken, Long id){

        return EditCommentServiceDto.builder()
                .id(id)
                .content(this.content)
                .accessToken(accessToken)
                .build();
    }
}
