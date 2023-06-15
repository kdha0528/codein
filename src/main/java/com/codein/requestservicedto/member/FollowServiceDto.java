package com.codein.requestservicedto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter
@ToString
public class FollowServiceDto {

    private final String accessToken;
    private final Long followingId;

    @Builder
    public FollowServiceDto(String accessToken, Long followingId) {
        this.accessToken = accessToken;
        this.followingId = followingId;
    }
}
