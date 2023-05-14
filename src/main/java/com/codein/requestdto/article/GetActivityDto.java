package com.codein.requestdto.article;

import lombok.Builder;
import lombok.Getter;


import java.util.Objects;

import static java.lang.Math.max;

@Getter
public class GetActivityDto {

    private static final int SIZE = 20;
    private final Long id;
    private final Integer page;
    private final Activity activity;


    @Builder
    public GetActivityDto(Long id, Integer page, String activity) {
        this.id = id;
        this.page = Objects.requireNonNullElse(page, 1);
        if(activity == null){
            this.activity = Activity.ARTICLES;
        }else{
            this.activity = Activity.valueOf(activity.toUpperCase());
        }
    }

    public long getOffset() {
        return (long) (max(1, page) - 1) * SIZE;
    }

    public int getSize() {
        return SIZE;
    }
}