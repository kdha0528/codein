package com.codein.domain.article;

public enum Category {

    NOTICE("공지사항"),
    COMMUNITY("커뮤니티"),
    QUESTION("Q&A"),
    INFORMATION("정보공유"),
    CODEIN("코드인");

    private final String category;

    Category(String category) {
        this.category = category;
    }

    public String getValue() {
        return category;
    }

    public String getName() {
        return this.name();
    }

}
