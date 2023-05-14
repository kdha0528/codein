package com.codein.requestdto.article;

public enum Activity {
    ARTICLES("ARTICLES"),
    COMMENTS("COMMENTS"),
    LIKES("LIKES");

    private final String activity;

    Activity(String activity) {
        this.activity = activity;
    }
    public String getValue() {
        return activity;
    }

    public String getName() {
        return this.name();
    }
}
