package com.codein.repository.comment.like;

import com.codein.domain.comment.Comment;
import com.codein.domain.comment.CommentLike;
import com.codein.domain.member.Member;

public interface CommentLikeRepositoryCustom {

     CommentLike existsCommentLike(Comment comment, Member member);
}
