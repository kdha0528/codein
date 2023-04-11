package com.codein.domain.image;

import com.codein.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfileImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imgFileName;
    private LocalDateTime create_date;
    @OneToOne(mappedBy = "profileImage")
    private Member member;


    @PrePersist
    public void createDate() {
        create_date = LocalDateTime.now();
    }

    @Builder
    public ProfileImage(String imgFileName) {
        this.imgFileName = imgFileName;
    }

}
