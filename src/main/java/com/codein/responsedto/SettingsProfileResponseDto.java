package com.codein.responsedto;

import com.codein.domain.image.ProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.IOException;

@Getter
@ToString
public class SettingsProfileResponseDto {

    private final String name;
    private final String nickname;
    private final String imagePath;

    @Builder
    public SettingsProfileResponseDto(String name, String nickname, ProfileImage profileImage) throws IOException {
        this.name = name;
        this.nickname = nickname;
        if (profileImage != null) {
            this.imagePath = "/my-backend-api/images/profile/" + profileImage.getImgFileName();
        } else {
            this.imagePath = null;
        }
    }
}
