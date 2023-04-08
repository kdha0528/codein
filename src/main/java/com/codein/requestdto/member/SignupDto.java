package com.codein.requestdto.member;

import com.codein.domain.member.Member;
import com.codein.requestservicedto.member.SignupServiceDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class SignupDto {
    @NotBlank(message = "이메일을 입력해주세요.")
    @Email(message = "이메일 형식을 맞춰주세요.")
    private final String email;
    @NotBlank(message = "닉네임을 입력해주세요.")
    @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,16}$", message = "2자 이상, 16자 이하, 영어 또는 숫자 또는 한글로 입력해주세요.")
    private final String nickname;
    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Pattern(regexp = "^[a-zA-Z0-9]{8,20}$", message = "비밀번호는 소문자, 대문자, 숫자를 이용하여 8~20글자 입력해주세요.")
    private final String password;
    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, max = 10)
    private final String name;
    @NotBlank(message = "전화번호를 입력해주세요.")
    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private final String phone;
    @NotBlank(message = "생년월일을 입력해주세요.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "올바른 형식으로 입력해주세요.")
    private final String birth;
    @NotBlank(message = "성별을 입력해주세요.")
    @Pattern(regexp = "^(?:male|female)$", message = "올바른 형식으로 입력해주세요.")
    private final String sex;

    @Builder
    public SignupDto(String email, String nickname, String password, String name, String phone, String birth, String sex) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
        this.sex = sex;
    }

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .name(name)
                .phone(phone)
                .birth(birth)
                .sex(sex)
                .build();
    }

    public SignupServiceDto toSignupServiceDto() {
        return SignupServiceDto.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .name(name)
                .phone(phone)
                .birth(birth)
                .sex(sex)
                .build();
    }

}
