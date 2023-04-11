package com.codein.requestdto.member;

import com.codein.requestservicedto.member.EmailServiceDto;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class EmailDto {

    @Email(message = "이메일 형식을 맞춰주세요.")
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
