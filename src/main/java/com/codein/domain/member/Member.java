package com.codein.domain.member;

import com.codein.domain.Session;
import com.codein.domain.image.ProfileImage;
import com.codein.error.exception.member.MemberNotLoginException;
import com.codein.responsedto.LoginResponseDto;
import com.codein.responsedto.ProfileResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    private final List<Session> sessions = new ArrayList<>();

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

    public LoginResponseDto toMemberResponse() {
        return LoginResponseDto.builder()
                .id(this.getId())
                .email(this.getEmail())
                .nickname(this.getNickname())
                .point(this.getPoint())
                .role(this.getRole())
                .build();
    }

    public ProfileResponseDto toProfileResponse() {
        return ProfileResponseDto.builder()
                .email(this.getEmail())
                .name(this.getName())
                .nickname(this.getNickname())
                .phone(this.getPhone())
                .build();
    }

    public void deleteSession(Session session) {
        boolean removed = sessions.removeIf(s -> s.equals(session));
        if (!removed) {
            throw new MemberNotLoginException();
        }
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
