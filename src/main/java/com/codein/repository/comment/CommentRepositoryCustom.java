package com.codein.repository.comment;

import com.codein.domain.article.Article;
import com.codein.domain.comment.Comment;
import com.codein.requestdto.comment.GetCommentListServiceDto;
import com.codein.responsedto.comment.CommentListResponseDto;

import java.util.List;

public interface CommentRepositoryCustom {

    Comment findTargetById(Long id);

    CommentListResponseDto getCommentList(GetCommentListServiceDto getCommentListServiceDto);

    List<Comment> findCommentsByArticle(Article article);


}
