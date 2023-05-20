package com.codein.domain.comment;

import com.codein.domain.article.Article;
import com.codein.domain.article.Category;
import com.codein.domain.member.Member;
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
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member recipient;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member commenter;

    @NotNull
    private String content;

    @NotNull
    private LocalDateTime createdAt;

    private boolean deleted;

    @Builder
    public Comment(Article article, Member recipient, Member commenter, String content) {
        this.article = article;
        this.recipient = recipient;
        this.commenter = commenter;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.deleted = false;
    }
}
