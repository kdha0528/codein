package com.codein.domain.article;

import com.codein.domain.member.Member;
import com.codein.responsedto.article.ActivityListItem;
import com.codein.responsedto.article.ArticleResponseData;
import com.codein.responsedto.article.ArticleListItem;
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
    @Column(columnDefinition = "TEXT")
    private String content;
    @NotNull
    private LocalDateTime createdAt;
    private LocalDateTime changedAt;
    private Integer viewNum;
    private Integer commentNum;
    private Integer likeNum;
    private Integer dislikeNum;
    private boolean deleted;
    private boolean changed;


    @Builder
    public Article(Member member, Category category, String title, String content) {
        this.member = member;
        this.category = category;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.changedAt = null;
        this.viewNum = 0;
        this.commentNum = 0;
        this.likeNum = 0;
        this.dislikeNum = 0;
        this.deleted = false;
        this.changed = false;
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

    public ArticleResponseData toArticleResponseData(boolean viewCount){
        return ArticleResponseData.builder()
                .id(this.id)
                .category(this.category)
                .title(this.title)
                .content(this.content)
                .viewNum(viewCount ? ++this.viewNum : this.viewNum)
                .commentNum(this.commentNum)
                .likeNum(this.likeNum)
                .dislikeNum(this.dislikeNum)
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

    public void increaseCommentNum() {
        this.commentNum++;
    }
    public void deleteArticle(){ this.deleted = true;}


}
