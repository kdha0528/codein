package com.codein.requestdto.article;

public enum Activity {
    ARTICLES("ARTICLES"),
    COMMENTS("COMMENTS"),
    LIKED_ARTICLES("LIKED_ARTICLES");
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
