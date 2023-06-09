package com.codein.domain.comment;

import com.codein.domain.article.Article;
import com.codein.domain.member.Member;
import com.codein.responsedto.comment.CommentListItem;
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

    private Long parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Article article;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment target;
    private String targetNickname;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    private LocalDateTime createdAt;
    private LocalDateTime changedAt;

    @NotNull
    private Integer likeNum;
    @NotNull
    private Integer dislikeNum;

    @NotNull
    private boolean deleted;
    @NotNull
    private boolean changed;

    @Builder
    public Comment(Article article, Long parentId, Comment target, String targetNickname, Member member, String content) {
        this.parentId = parentId;
        this.article = article;
        this.target = target;
        this.targetNickname = targetNickname;
        this.member = member;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.changedAt = null;
        this.likeNum = 0;
        this.dislikeNum = 0;
        this.deleted = false;
        this.changed = false;
    }


    public CommentListItem toCommentListItem() {
        Long targetMemberId = null;
        String targetNickname = null;
        if(this.target != null) {
            targetNickname = this.targetNickname;
            targetMemberId = this.target.member.getId();
        }
        return  CommentListItem.builder()
                .id(this.id)
                .content(this.content)
                .createdAt(this.createdAt)
                .parentId(this.parentId)
                .likeNum(this.likeNum)
                .dislikeNum(this.dislikeNum)
                .commenterId(this.member.getId())
                .commenterNickname(this.member.getNickname())
                .commenterProfileImage(this.member.getProfileImage())
                .targetMemberId(targetMemberId)
                .targetNickname(targetNickname)
                .deleted(isDeleted())
                .build();
    }

    public Comment edit(CommentEditor commentEditor){
        this.content = commentEditor.getContent();
        return this;
    }

    public void changeLikeNum(Integer changes){
        this.likeNum += changes;
    }
    public void changeDislikeNum(Integer changes) {
        this.dislikeNum += changes;
    }

    public void deleteComment() { this.deleted = true;}
}
