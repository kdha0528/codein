package com.codein.repository.comment.like;

import com.codein.domain.comment.Comment;
import com.codein.domain.comment.CommentLike;
import com.codein.domain.member.Member;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codein.domain.comment.QCommentLike.commentLike;

@Repository
@RequiredArgsConstructor
public class CommentLikeRepositoryCustomImpl  implements CommentLikeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public CommentLike existsCommentLike(Comment comment, Member member) {
        return jpaQueryFactory.selectFrom(commentLike)
                .where(commentLike.comment.eq(comment)
                        .and(commentLike.member.eq(member)))
                .fetchFirst();
    }
}
