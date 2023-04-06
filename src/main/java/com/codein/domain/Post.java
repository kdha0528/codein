package com.codein.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;
    @NotNull
    private Category category;
    @NotNull
    private String title;
    @NotNull
    private String content;
    private Integer viewNum;
    private Integer commentNum;
    private Integer likeNum;
    @NotNull
    private LocalDateTime createdAt;


    @Builder
    public Post(Member member, Category category, String title, String content) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.viewNum = 0;
        this.commentNum = 0;
        this.likeNum = 0;
        this.createdAt = LocalDateTime.now();
    }

}
