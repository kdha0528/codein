package com.codein.domain.article;

import com.codein.domain.member.Member;
import com.codein.responsedto.article.ActivityListItem;
import com.codein.responsedto.article.ArticleListItem;
import com.codein.responsedto.article.GetArticleResponseDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
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
    private Integer viewNum;
    private Integer commentNum;
    private Integer likeNum;
    private Integer dislikeNum;
    private boolean deleted;


    @Builder
    public Article(Member member, Category category, String title, String content) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.viewNum = 0;
        this.commentNum = 0;
        this.likeNum = 0;
        this.dislikeNum = 0;
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
                .id(this.id)
                .title(this.title)
                .authorId(this.member.getId())
                .profileImage(this.member.getProfileImage())
                .nickname(this.member.getNickname())
                .createdAt(this.createdAt)
                .viewNum(this.viewNum)
                .commentNum(this.commentNum)
                .likeNum(this.likeNum)
                .deleted(this.deleted)
                .build();
    }

    public ActivityListItem toActivityListItem(){
        return ActivityListItem.builder()
                .id(this.id)
                .authorId(this.member.getId())
                .category(this.category.getName())
                .title(this.title)
                .nickname(this.member.getNickname())
                .createdAt(this.createdAt)
                .viewNum(this.viewNum)
                .commentNum(this.commentNum)
                .likeNum(this.likeNum)
                .deleted(this.deleted)
                .build();
    }

    public GetArticleResponseDto toGetArticleResponseDto(boolean viewCount){
        return GetArticleResponseDto.builder()
                .id(this.id)
                .category(this.category)
                .title(this.title)
                .content(this.content)
                .viewNum(viewCount ? ++this.viewNum : this.viewNum)
                .commentNum(this.commentNum)
                .likeNum(this.likeNum)
                .createdAt(this.createdAt)
                .authorId(this.member.getId())
                .nickname(this.member.getNickname())
                .profileImage(this.member.getProfileImage())
                .deleted(this.deleted)
                .build();
    }

    public void changeLikeNum(Integer changes){
        this.likeNum += changes;
    }
    public void changeDislikeNum(Integer changes) {
        this.dislikeNum += changes;
    }

    public void deleteArticle(){ this.deleted = true;}


}
