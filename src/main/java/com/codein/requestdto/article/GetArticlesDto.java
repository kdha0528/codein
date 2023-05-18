package com.codein.requestdto.article;

import com.codein.domain.article.Category;
import com.codein.requestservicedto.article.GetArticlesServiceDto;
import lombok.Builder;
import lombok.Getter;
import java.util.Objects;


@Getter
public class GetArticlesDto {

    private static final int SIZE = 20;
    private final Category category;
    private final Integer page;
    private final Period period;
    private final Sort sort;
    // condition과 keyword는 검색 조건과, 검색문입니다.
    private final Condition condition;
    private final String keyword;

    @Builder
    public GetArticlesDto(String category, Integer page, String period, String sort, String condition, String keyword) {
        if(category == null){
            this.category = Category.COMMUNITY;
        } else {
            this.category = Category.valueOf(category.toUpperCase());
        }

        this.page = Objects.requireNonNullElse(page, 1);
        if(period == null){
            this.period = Period.ALL;
        }else{
            this.period = Period.valueOf(period);
        }
        if(sort == null){
            this.sort = Sort.LATEST;
        }else{
            this.sort = Sort.valueOf(sort);
        }
        if(condition == null){
            this.condition = Condition.ALL;
        } else{
            this.condition = Condition.valueOf(condition);
        }
        this.keyword = keyword;
    }

    public GetArticlesServiceDto toGetArticlesServiceDto(){
        return GetArticlesServiceDto.builder()
                .keyword(this.keyword)
                .page(this.page)
                .condition(this.condition)
                .sort(this.sort)
                .category(this.category)
                .period(this.period)
                .build();
    }

}
