package com.codein.domain;

import com.codein.error.exception.MemberNotLoginException;
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
    @Setter
    @NotNull
    private String email;
    @Setter
    @NotNull
    private String password;
    @Setter
    @NotNull
    private String name;
    @Column(unique = true)
    @Setter
    @NotNull
    private String nickname;
    @Setter
    @NotNull
    @Column(unique = true)
    private String phone;
    @NotNull
    private String birth;
    @NotNull
    private String sex;

    @NotNull
    private LocalDateTime createdAt;

    @Setter
    private Long point;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member", fetch = FetchType.LAZY)
    private final List<Session> sessions = new ArrayList<>();

    @Setter
    private Role role = Role.MEMBER;

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
        this.point = 0L;
    }

    public LoginResponseDto changeMemberResponse() {
        return LoginResponseDto.builder()
                .id(this.getId())
                .email(this.getEmail())
                .nickname(this.getNickname())
                .point(this.getPoint())
                .role(this.getRole())
                .build();
    }

    public ProfileResponseDto changeProfileResponse() {
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

    public MemberEditor.MemberEditorBuilder toEditor() {
        return MemberEditor.builder()
                .email(email)
                .password(password)
                .name(name)
                .nickname(nickname)
                .phone(phone);
    }

    public void edit(MemberEditor memberEditor) {
        this.email = memberEditor.getEmail();
        this.password = memberEditor.getPassword();
        this.name = memberEditor.getName();
        this.nickname = memberEditor.getNickname();
        this.phone = memberEditor.getPhone();
    }

    public void encryptPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }
}
