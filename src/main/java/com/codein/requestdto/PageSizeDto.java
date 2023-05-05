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
    private final Integer period = 0;

    @Builder.Default
    private final Integer sort = 0;

    public long getOffset() {
        return (long) (max(1, page) - 1) * SIZE;
    }

    public int getSize() {
        return SIZE;
    }

    public LocalDateTime getStartDate() {
        switch (this.period) {
            case 1 -> {
                return LocalDateTime.now().minusDays(1);
            }
            case 2 -> {
                return LocalDateTime.now().minusWeeks(1);
            }
            case 3 -> {
                return LocalDateTime.now().minusMonths(1);
            }
            case 4 -> {
                return LocalDateTime.now().minusYears(1);
            }
            default -> {
                return LocalDateTime.now().minusYears(1000);
            }
        }

    }

}
