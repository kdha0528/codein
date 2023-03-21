package com.codein.domain;

import com.codein.response.MemberResponse;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private String email;

    private String password;

    private String name;

    @Column(unique = true)
    private String phone;

    private String birth;

    private String sex;

    private LocalDateTime createdAt;

    private Long point;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member", fetch = FetchType.EAGER)
    private final List<Session> sessions = new ArrayList<>();

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
}
