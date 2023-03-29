package com.codein.domain;

import com.codein.error.exception.MemberNotLoginException;
import com.codein.responsedto.MemberResponseDto;
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
    public Member(String email, String password, String name, String phone, String birth, String sex) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.birth = birth;
        this.sex = sex;
        this.createdAt = LocalDateTime.now();
        this.point = 0L;
    }

    public MemberResponseDto changeMemberResponse() {
        return new MemberResponseDto(this);
    }


    public void addSession(Session session) {
        this.sessions.add(session);
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
                .phone(phone);
    }

    public void edit(MemberEditor memberEditor) {
        email = memberEditor.getEmail();
        password = memberEditor.getPassword();
        name = memberEditor.getName();
        phone = memberEditor.getPhone();
    }

    public void encryptPassword(String encryptedPassword) {
        this.password = encryptedPassword;
    }
}
