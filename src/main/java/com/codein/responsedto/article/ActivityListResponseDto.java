package com.codein.responsedto.article;

import com.codein.domain.image.ProfileImage;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@ToString
@Getter
public class ActivityListResponseDto {
    private final Long id;
    private final String nickname;
    private final String imagePath;
    private final Boolean isFollow;
    private final List<ActivityListItem> activityList;
    private final int maxPage;

    @Builder
    public ActivityListResponseDto(Long id, String nickname, ProfileImage profileImage, List<ActivityListItem> activityList, int maxPage, Boolean isFollow) {
        this.id = id;
        this.nickname = nickname;
        this.activityList = activityList;
        this.maxPage = maxPage;
        this.isFollow = isFollow;
        if (profileImage != null) {
            this.imagePath = "https://d32r1r4pmjmgdj.cloudfront.net/images/profile/" + profileImage.getImgFileName();
        } else {
            this.imagePath = null;
        }
    }
}
