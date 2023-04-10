package com.codein.domain.article;

import lombok.Getter;

@Getter
public class ArticleEditor {

    private final String category;
    private final String title;
    private final String content;

    public ArticleEditor(ArticleEditorBuilder articleEditorBuilder) {
        this.category = articleEditorBuilder.category;
        this.title = articleEditorBuilder.title;
        this.content = articleEditorBuilder.content;
    }

    public static ArticleEditorBuilder builder() {
        return new ArticleEditorBuilder();
    }

    @Getter
    public static class ArticleEditorBuilder {
        private String category;
        private String title;
        private String content;

        public ArticleEditorBuilder() {
        }

        public ArticleEditorBuilder category(String category) {
            this.category = category;
            return this;
        }

        public ArticleEditorBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ArticleEditorBuilder content(String content) {
            this.content = content;
            return this;
        }

        public ArticleEditor build() {
            return new ArticleEditor(this);
        }
    }
}
