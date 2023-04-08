package com.codein.domain.post;

import lombok.Getter;

@Getter
public class PostEditor {

    private final String category;
    private final String title;
    private final String content;

    public PostEditor(PostEditorBuilder postEditorBuilder) {
        this.category = postEditorBuilder.category;
        this.title = postEditorBuilder.title;
        this.content = postEditorBuilder.content;
    }

    public static PostEditorBuilder builder() {
        return new PostEditorBuilder();
    }

    @Getter
    public static class PostEditorBuilder {
        private String category;
        private String title;
        private String content;

        public PostEditorBuilder() {
        }

        public PostEditorBuilder category(String category) {
            this.category = category;
            return this;
        }

        public PostEditorBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PostEditorBuilder content(String content) {
            this.content = content;
            return this;
        }

        public PostEditor build() {
            return new PostEditor(this);
        }
    }
}
