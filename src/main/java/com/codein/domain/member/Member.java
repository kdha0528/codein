package com.codein.domain.member;

import com.codein.domain.auth.Tokens;
import com.codein.domain.image.ProfileImage;
import com.codein.responsedto.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private String email;
    @NotNull
    private String password;

    @NotNull
    private String name;
    @Column(unique = true)
    @NotNull
    private String nickname;

    @NotNull
    @Column(unique = true)
    private String phone;
    @NotNull
    private String birth;
    @NotNull
    private String sex;
    @NotNull
    private LocalDateTime createdAt;

    private Integer point;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member", fetch = FetchType.LAZY)
    private final List<Tokens> tokens = new ArrayList<>();

    @Setter
    private Role role = Role.MEMBER;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileImage profileImage;

    @Builder
    public Member(String email, String password, String name, String nickname, String phone, String birth, String sex) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.birth = birth;
        this.sex = sex;
        this.createdAt = LocalDateTime.now();
        this.point = 0;
    }

    public MemberListResponseDto toMemberResponseDto() {
        return MemberListResponseDto.builder()
                .id(this.getId())
                .email(this.getEmail())
                .name(this.getName())
                .build();
    }

    public LoginResponseDto toLoginResponseDto() {
        return LoginResponseDto.builder()
                .id(this.getId())
                .email(this.getEmail())
                .nickname(this.getNickname())
                .role(this.getRole().getRole())
                .point(this.getPoint())
                .build();
    }

    public MemberProfileResponseDto toProfileResponseDto() {
        return MemberProfileResponseDto.builder()
                .email(this.getEmail())
                .name(this.getName())
                .nickname(this.getNickname())
                .phone(this.getPhone())
                .build();
    }

    public SettingsProfileResponseDto toSettingsProfileResponseDto() throws IOException {
        return SettingsProfileResponseDto.builder()
                .name(this.getName())
                .nickname(this.getNickname())
                .profileImage(this.getProfileImage())
                .build();
    }

    public SettingsAccountResponseDto toSettingsAccountResponseDto() throws IOException {
        return SettingsAccountResponseDto.builder()
                .email(this.getEmail())
                .phone(this.getPhone())
                .build();
    }

    public ProfileEditor.ProfileEditorBuilder toProfileEditor() {
        if (profileImage == null) {
            return ProfileEditor.builder()
                    .name(name)
                    .nickname(nickname)
                    .imgFileName(null);
        } else {
            return ProfileEditor.builder()
                    .name(name)
                    .nickname(nickname)
                    .imgFileName(profileImage.getImgFileName());
        }
    }

    public void editProfile(ProfileEditor profileEditor) {
        this.name = profileEditor.getName();
        this.nickname = profileEditor.getNickname();
        if (profileEditor.getImgFileName() != null) {
            if (profileImage != null) {
                this.setProfileImage(null);
            }
            this.profileImage = ProfileImage.builder()
                    .imgFileName(profileEditor.getImgFileName())
                    .build();
        }
    }

    public void setProfileImage(ProfileImage profileImage) {
        this.profileImage = profileImage;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}
