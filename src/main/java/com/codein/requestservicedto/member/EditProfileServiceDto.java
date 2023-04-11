package com.codein.requestservicedto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
public class EditProfileServiceDto {

    private final String name;
    private final String nickname;
    private final MultipartFile profileImage;

    @Builder
    public EditProfileServiceDto(String name, String nickname, MultipartFile profileImage) {
        this.name = name;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }
}
