package com.codein.domain.comment;

import com.codein.domain.utils.LikeChanges;
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
public class CommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    private boolean isLike;
    private boolean isDislike;

    @NotNull
    private LocalDateTime likedAt;

    @Builder
    public CommentLike(Comment comment, Member member, boolean isLike) {
        this.comment = comment;
        this.member = member;
        if(isLike){
            this.isLike = true;
            this.isDislike = false;
        } else {
            this.isLike = false;
            this.isDislike = true;
        }
        this.likedAt = LocalDateTime.now();
    }

    public LikeChanges change(boolean isLike) {
        LikeChanges result = new LikeChanges();

        if(isLike){ // 클라이언트가 추천을 눌렀을 때
            if(this.isLike){    // 이미 추천이 눌려있다면 추천을 취소
                this.isLike = false;
                result.decreaseLike();
            } else {
                if (this.isDislike){ // 비추천이 눌려있다면, 비추천을 취소하고 추천
                    this.isDislike = false;
                    result.decreaseDislike();
                }
                this.isLike = true; // 비추천이 안눌려있으면 그냥 추천
                result.increaseLike();
            }
        } else {    // 클라이언트가 비추천을 누렀을 때
            if(this.isDislike){ // 이미 비추천이 눌려있다면 비추천을 취소
                this.isDislike = false;
                result.decreaseDislike();
            } else {
                if(this.isLike){ // 추천이 눌려있다면, 추천을 취소하고 비추천
                    this.isLike = false;
                    result.decreaseLike();
                }
                this.isDislike = true;    // 추천이 안눌려있으면 그냥 비추천
                result.increaseDislike();
            }
        }
        return result;
    }

}
