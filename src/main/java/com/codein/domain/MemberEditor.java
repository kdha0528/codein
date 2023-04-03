package com.codein.domain;

import lombok.Getter;

@Getter
public class MemberEditor {

    private final String email;
    private final String nickname;
    private final String password;
    private final String name;
    private final String phone;

    public MemberEditor(MemberEditorBuilder memberEditorBuilder) {
        this.email = memberEditorBuilder.email;
        this.nickname = memberEditorBuilder.nickname;
        this.password = memberEditorBuilder.password;
        this.name = memberEditorBuilder.name;
        this.phone = memberEditorBuilder.phone;
    }

    public static MemberEditorBuilder builder() {
        return new MemberEditorBuilder();
    }

    @Getter
    public static class MemberEditorBuilder {
        private String email;
        private String nickname;
        private String password;
        private String name;
        private String phone;

        MemberEditorBuilder() {
        }

        public MemberEditorBuilder email(final String email) {
            if (email != null) {
                this.email = email;
            }
            return this;
        }

        public MemberEditorBuilder nickname(final String nickname) {
            if (nickname != null) {
                this.nickname = nickname;
            }
            return this;
        }

        public MemberEditorBuilder password(final String password) {
            if (password != null) {
                this.password = password;
            }
            return this;
        }

        public MemberEditorBuilder name(final String name) {
            if (name != null) {
                this.name = name;
            }
            return this;
        }

        public MemberEditorBuilder phone(final String phone) {
            if (phone != null) {
                this.phone = phone;
            }
            return this;
        }

        public MemberEditor build() {
            return new MemberEditor(this);
        }
    }
}
