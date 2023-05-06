package com.codein.requestdto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.lang.Math.max;

@Getter
public class GetArticlesDto {

    private static final int SIZE = 20;
    private final Integer page;
    private final String period;
    private final String sort;
    // condition과 keyword는 검색 조건과, 검색문입니다.
    private final String condition;
    private final String keyword;

    @Builder
    public GetArticlesDto(Integer page, String period, String sort, String condition, String keyword) {
        this.page = Objects.requireNonNullElse(page, 1);
        this.period = Objects.requireNonNullElse(period, "all");
        this.sort = Objects.requireNonNullElse(sort, "latest");
        this.condition = condition;
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
            case "all" ->{
                return LocalDateTime.now().minusYears(1000);
            }
            case "1d" -> {
                return LocalDateTime.now().minusDays(1);
            }
            case "1w" -> {
                return LocalDateTime.now().minusWeeks(1);
            }
            case "1m" -> {
                return LocalDateTime.now().minusMonths(1);
            }
            case "1y" -> {
                return LocalDateTime.now().minusYears(1);
            }
            default -> {
                return LocalDateTime.now();
            }
        }
    }

}
