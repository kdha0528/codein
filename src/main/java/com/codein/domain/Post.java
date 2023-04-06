package com.codein.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Member member;
    @NotNull
    private String category;
    @NotNull
    private String title;
    @NotNull
    private String contents;

    private Integer viewNum;

    private Integer commentNum;

    private Integer likeNum;
    @NotNull
    private LocalDateTime createdAt;

    @Builder
    public Post(Member member, String category, String title, String contents) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.contents = contents;
        this.viewNum = 0;
        this.commentNum = 0;
        this.likeNum = 0;
        this.createdAt = LocalDateTime.now();
    }
}
