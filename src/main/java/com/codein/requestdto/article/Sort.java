package com.codein.requestdto.article;

public enum Sort {
    LATEST("LATEST"),
    VIEW("VIEW"),
    LIKE("LIKE");

    private final String sort;

    Sort(String sort) {
        this.sort = sort;
    }
    public String getValue() {
        return sort;
    }

    public String getName() {
        return this.name();
    }

}
