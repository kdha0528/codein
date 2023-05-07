package com.codein.requestdto.article;

public enum Condition {

    ALL("ALL"),
    TITLE("TITLE"),
    AUTHOR("AUTHOR");

    private final String condition;

    Condition(String condition) {
        this.condition = condition;
    }
    public String getValue() {
        return condition;
    }

    public String getName() {
        return this.name();
    }
}
