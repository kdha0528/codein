package com.codein.requestservicedto.article;

import com.codein.domain.article.Category;
import com.codein.requestdto.article.Condition;
import com.codein.requestdto.article.Period;
import com.codein.requestdto.article.Sort;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

import static java.lang.Math.max;

@Getter
@ToString
public class GetArticlesServiceDto {
    private static final int SIZE = 20;
    private final Category category;
    private final Integer page;
    private final Period period;
    private final Sort sort;
    private final Condition condition;
    private final String keyword;

    @Builder
    public GetArticlesServiceDto(Category category, Integer page, Period period, Sort sort, Condition condition, String keyword) {
        this.category = category;
        this.page = page;
        this.period = period;
        this.sort = sort;
        this.condition = condition;
        this.keyword = keyword;
    }

    public int getSize() {
        return SIZE;
    }

    public long getOffset() {
        return (long) (max(1, page) - 1) * SIZE;
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
