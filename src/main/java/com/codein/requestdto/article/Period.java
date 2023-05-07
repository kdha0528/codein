package com.codein.requestdto.article;

public enum Period {

    ALL("ALL"),
    DAY("DAY"),
    WEEK("WEEK"),
    MONTH("MONTH"),
    YEAR("YEAR");


    private final String period;

    Period(String period) {
        this.period = period;
    }
    public String getValue() {
        return period;
    }

    public String getName() {
        return this.name();
    }
}
