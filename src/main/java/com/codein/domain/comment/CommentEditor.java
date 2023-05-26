package com.codein.domain.comment;

import lombok.Getter;

@Getter
public class CommentEditor {

    private final String content;

    public CommentEditor(CommentEditorBuilder commentEditorBuilder) {
        this.content = commentEditorBuilder.content;
    }

    public static CommentEditorBuilder builder() { return new CommentEditorBuilder(); }

    @Getter
    public static class CommentEditorBuilder {
        private String content;

        public CommentEditorBuilder(){}

        public CommentEditorBuilder content(String content) {
            this.content = content;
            return this;
        }

        public CommentEditor build() { return new CommentEditor(this);}

    }
}
