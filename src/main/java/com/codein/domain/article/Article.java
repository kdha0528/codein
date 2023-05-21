package com.codein.domain.article;

import com.codein.domain.member.Member;
import com.codein.requestservicedto.article.GetArticleServiceDto;
import com.codein.responsedto.article.ActivityListItem;
import com.codein.responsedto.article.ArticleListItem;
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
public class Article {

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
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @NotNull
    private LocalDateTime createdAt;
    private boolean deleted;


    @Builder
    public Article(Member member, Category category, String title, String content) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.deleted = false;
    }

    public Article edit(ArticleEditor articleEditor) {
        this.category = Category.valueOf(articleEditor.getCategory());
        this.title = articleEditor.getTitle();
        this.content = articleEditor.getContent();
        return this;
    }

    public ArticleListItem toArticleListItem(){
        return ArticleListItem.builder()
                .id(this.getId())
                .title(this.getTitle())
                .authorId(this.member.getId())
                .profileImage(this.member.getProfileImage())
                .nickname(this.member.getNickname())
                .createdAt(this.getCreatedAt())
                .build();
    }

    public ActivityListItem toActivityListItem(){
        return ActivityListItem.builder()
                .id(this.getId())
                .authorId(this.member.getId())
                .category(this.category.getName())
                .title(this.getTitle())
                .nickname(this.member.getNickname())
                .createdAt(this.getCreatedAt())
                .build();
    }

    public GetArticleServiceDto toGetArticleServiceDto(){
        return GetArticleServiceDto.builder()
                .id(this.id)
                .category(this.category.getName())
                .title(this.title)
                .content(this.content)
                .createdAt(this.createdAt)
                .authorId(this.member.getId())
                .nickname(this.member.getNickname())
                .profileImage(this.member.getProfileImage())
                .deleted(this.deleted)
                .build();
    }

}
