package com.codein.responsedto.member;

import com.codein.domain.image.ProfileImage;
import com.codein.domain.member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MemberResponseDto {

    private final Long id;
    private final String nickname;
    private final String role;
    private final String imagePath;

    @Builder
    public MemberResponseDto(Long id, String nickname, Role role, ProfileImage profileImage) {
        this.id = id;
        this.nickname = nickname;
        this.role = role.getRole();
        if (profileImage != null) {
            this.imagePath = "/my-backend-api/images/profile/" + profileImage.getImgFileName();
        } else {
            this.imagePath = null;
        }
    }

}
