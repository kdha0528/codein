package com.codein.requestdto.member;

import com.codein.requestservicedto.member.EmailServiceDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.web.bind.annotation.ModelAttribute;

@Getter
@ToString
@NoArgsConstructor
public class EmailDto {

    @Email(message = "이메일 형식을 맞춰주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Builder
    public EmailDto(String email) {
        this.email = email;
    }


    public EmailServiceDto toEmailServiceDto() {
        return EmailServiceDto.builder()
                .email(this.email)
                .build();
    }
}
