package com.codein.requestdto.member;

import com.codein.requestservicedto.member.EditProfileServiceDto;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
public class EditProfileDto {

    @Size(min = 2, max = 10)
    private final String name;
    @Pattern(regexp = "^(?=.*[a-z0-9가-힣])[a-z0-9가-힣]{2,8}$", message = "2자 이상, 16자 이하, 영어 또는 숫자 또는 한글로 입력해주세요.")
    private final String nickname;
    private final MultipartFile profileImage;

    @Builder
    public EditProfileDto(String name, String nickname, MultipartFile profileImage) {
        this.name = name;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public EditProfileServiceDto toEditProfileServiceDto() {
        return EditProfileServiceDto.builder()
                .name(this.name)
                .nickname(this.nickname)
                .profileImage(this.profileImage)
                .build();
    }
}
