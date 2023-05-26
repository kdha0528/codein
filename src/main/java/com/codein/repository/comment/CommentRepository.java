package com.codein.repository.comment;


import com.codein.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository  extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
