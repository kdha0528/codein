package com.codein.requestdto.article;

import com.codein.requestservicedto.article.GetActivitiesServiceDto;
import lombok.Builder;
import lombok.Getter;


import java.util.Objects;

import static java.lang.Math.max;

@Getter
public class GetActivitiesDto {

    private static final int SIZE = 20;
    private final Long id;
    private final Integer page;
    private final Activity activity;


    @Builder
    public GetActivitiesDto(Long id, Integer page, String activity) {
        this.id = id;
        this.page = Objects.requireNonNullElse(page, 1);
        if(activity == null){
            this.activity = Activity.ARTICLES;
        }else{
            this.activity = Activity.valueOf(activity.toUpperCase());
        }
    }

    public GetActivitiesServiceDto toGetActivitiesServiceDto(){
        return GetActivitiesServiceDto.builder()
                .activity(this.activity)
                .page(this.page)
                .id(this.id)
                .build();
    }
}