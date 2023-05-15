package com.codein.requestdto.article;

import com.codein.domain.article.Category;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.lang.Math.max;

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

    public long getOffset() {
        return (long) (max(1, page) - 1) * SIZE;
    }

    public int getSize() {
        return SIZE;
    }

    public LocalDateTime getStartDate() {
        switch (this.period) {
            case ALL ->{
                return LocalDateTime.now().minusYears(100);
            }
            case DAY -> {
                return LocalDateTime.now().minusDays(1);
            }
            case WEEK -> {
                return LocalDateTime.now().minusWeeks(1);
            }
            case MONTH -> {
                return LocalDateTime.now().minusMonths(1);
            }
            case YEAR -> {
                return LocalDateTime.now().minusYears(1);
            }
            default -> {
                return LocalDateTime.now();
            }
        }
    }

}
