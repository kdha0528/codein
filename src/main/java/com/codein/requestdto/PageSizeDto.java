package com.codein.requestdto;

import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Getter
@Builder
public class PageSizeDto {

    private static final int MAX_SIZE = 20000;

    @Builder.Default
    private final Integer page = 1;

    @Builder.Default
    private final Integer size = 20;

    public long getOffset() {
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }

}
