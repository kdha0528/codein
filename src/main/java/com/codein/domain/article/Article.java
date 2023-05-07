package com.codein.domain.article;

import com.codein.domain.member.Member;
import com.codein.responsedto.ArticleResponseDto;
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
    private String content;
    private Integer viewNum;
    private Integer commentNum;
    private Integer likeNum;
    @NotNull
    private LocalDateTime createdAt;


    @Builder
    public Article(Member member, Category category, String title, String content, Integer viewNum, Integer commentNum, Integer likeNum) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.viewNum = (viewNum == null) ? 0 : viewNum;
        this.commentNum = (commentNum == null) ? 0 : commentNum;
        this.likeNum = (likeNum == null) ? 0 : likeNum;
        this.createdAt = LocalDateTime.now();
    }

    public void edit(ArticleEditor articleEditor) {
        this.category = Category.valueOf(articleEditor.getCategory());
        this.title = articleEditor.getTitle();
        this.content = articleEditor.getContent();
    }

    public ArticleResponseDto toArticleListResponseDto(){
        return ArticleResponseDto.builder()
                .id(this.getId())
                .title(this.getTitle())
                .profileImage(this.member.getProfileImage())
                .nickname(this.member.getNickname())
                .createdAt(this.getCreatedAt())
                .commentNum(this.getCommentNum())
                .likeNum(this.getLikeNum())
                .viewNum(this.getViewNum())
                .build();
    }
}
