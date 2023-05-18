package com.codein.responsedto.article;

import com.codein.domain.image.ProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
public class ActivityListResponseDto {
    private final Long id;
    private final String nickname;
    private final String imagePath;
    private final List<ActivityListItem> activityList;
    private final int maxPage;

    @Builder
    public ActivityListResponseDto(Long id, String nickname, ProfileImage profileImage, List<ActivityListItem> activityList, int maxPage) {
        this.id = id;
        this.nickname = nickname;
        this.activityList = activityList;
        this.maxPage = maxPage;
        if (profileImage != null) {
            this.imagePath = "/my-backend-api/images/profile/" + profileImage.getImgFileName();
        } else {
            this.imagePath = null;
        }
    }
}
