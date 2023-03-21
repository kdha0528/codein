package com.codein.domain;

import com.codein.exception.NotSigninedAccount;
import com.codein.response.MemberResponse;
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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member", fetch = FetchType.EAGER, orphanRemoval = true)
    private final List<Session> sessions = new ArrayList<>();

    @Setter
    @NotNull
    private Role role;

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
        this.role = Role.ADMIN;
    }

    public MemberResponse changeMemberResponse() {
        return new MemberResponse(this);
    }


    public Session addSession() {
        Session session = Session.builder()
                .member(this)
                .build();
        this.sessions.add(session);

        return session;
    }

    public void deleteSession(Session session) {
        boolean removed = sessions.removeIf(s -> s.equals(session));
        if (!removed) {
            throw new NotSigninedAccount();
        }
    }
}
