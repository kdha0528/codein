package com.codein.requestdto;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

import static java.lang.Math.max;

@Getter
@Builder
public class PageSizeDto {

    private static final int SIZE = 20;

    @Builder.Default
    private final Integer page = 1;

    @Builder.Default
    private final String period = "all";

    @Builder.Default
    private final String sort = "latest";

    public long getOffset() {
        return (long) (max(1, page) - 1) * SIZE;
    }

    public int getSize() {
        return SIZE;
    }

    public LocalDateTime getStartDate() {
        switch (this.period) {
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
                return LocalDateTime.now().minusYears(1000);
            }
        }

    }

}
