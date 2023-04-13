package com.codein.responsedto;

import com.codein.domain.image.ProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Getter
@ToString
public class ProfileSettingsResponseDto {

    private final String name;
    private final String nickname;
    private final byte[] profileImage;

    @Builder
    public ProfileSettingsResponseDto(String name, String nickname, ProfileImage profileImage) throws IOException {
        this.name = name;
        this.nickname = nickname;
        if (profileImage != null) {
            this.profileImage = getProfileImageBytes(profileImage.getImgFileName());
        } else {
            this.profileImage = null;
        }
    }

    public byte[] getProfileImageBytes(String profileImageName) throws IOException {
        InputStream inputStream = new FileInputStream("C:\\workspace\\springboot\\codein\\image\\profile\\" + profileImageName);
        return IOUtils.toByteArray(inputStream);
    }
}
