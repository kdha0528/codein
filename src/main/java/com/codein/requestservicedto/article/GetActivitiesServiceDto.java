package com.codein.requestservicedto.article;

import com.codein.requestdto.article.Activity;
import jakarta.servlet.http.Cookie;
import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

import static java.lang.Math.max;

@Getter
public class GetActivitiesServiceDto {
    private static final int SIZE = 20;
    private final Long id;
    private final Integer page;
    private final Activity activity;
    private final String accessToken;

    @Builder
    public GetActivitiesServiceDto(Long id, Integer page, Activity activity, Cookie cookie) {
        this.id = id;
        this.page = page;
        this.activity = activity;
        if(cookie == null){
            this.accessToken = null;
        } else {
            this.accessToken = cookie.getValue();
        }
    }


    public long getOffset() {
        return (long) (max(1, page) - 1) * SIZE;
    }

    public int getSize() {
        return SIZE;
    }
}
