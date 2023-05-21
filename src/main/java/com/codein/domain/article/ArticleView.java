package com.codein.domain.article;

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
public class ArticleView {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    @NotNull
    private String clientIp;

    @NotNull
    private LocalDateTime viewedAt;
    @Builder
    public ArticleView(Article article, String clientIp) {
        this.article = article;
        this.clientIp = clientIp;
        this.viewedAt = LocalDateTime.now();
    }
}
