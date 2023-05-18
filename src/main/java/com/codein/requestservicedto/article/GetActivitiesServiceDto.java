package com.codein.requestservicedto.article;

import com.codein.requestdto.article.Activity;
import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.max;

@Getter
public class GetActivitiesServiceDto {
    private static final int SIZE = 20;
    private final Long id;
    private final Integer page;
    private final Activity activity;

    @Builder
    public GetActivitiesServiceDto(Long id, Integer page, Activity activity) {
        this.id = id;
        this.page = page;
        this.activity = activity;
    }


    public long getOffset() {
        return (long) (max(1, page) - 1) * SIZE;
    }

    public int getSize() {
        return SIZE;
    }
}
