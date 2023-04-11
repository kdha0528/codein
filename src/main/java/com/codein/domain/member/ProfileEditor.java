package com.codein.domain.member;

import lombok.Getter;

@Getter
public class ProfileEditor {
    private final String name;
    private final String nickname;
    private final String imgFileName;

    public ProfileEditor(ProfileEditor.ProfileEditorBuilder profileEditorBuilder) {
        this.name = profileEditorBuilder.name;
        this.nickname = profileEditorBuilder.nickname;
        this.imgFileName = profileEditorBuilder.imgFileName;
    }

    public static ProfileEditor.ProfileEditorBuilder builder() {
        return new ProfileEditor.ProfileEditorBuilder();
    }


    @Getter
    public static class ProfileEditorBuilder {

        private String name;
        private String nickname;
        private String imgFileName;

        ProfileEditorBuilder() {
        }

        public ProfileEditor.ProfileEditorBuilder name(final String name) {
            if (name != null) {
                this.name = name;
            }
            return this;
        }

        public ProfileEditor.ProfileEditorBuilder nickname(final String nickname) {
            if (nickname != null) {
                this.nickname = nickname;
            }
            return this;
        }

        public ProfileEditor.ProfileEditorBuilder imgFileName(final String imgFileName) {
            if (imgFileName != null) {
                this.imgFileName = imgFileName;
            }
            return this;
        }

        public ProfileEditor build() {
            return new ProfileEditor(this);
        }
    }


}
