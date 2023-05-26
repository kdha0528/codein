package com.codein.repository.comment;

import com.codein.domain.comment.Comment;
import com.codein.requestdto.comment.GetCommentListServiceDto;
import com.codein.responsedto.comment.CommentListResponseDto;

public interface CommentRepositoryCustom {

    Comment findTargetById(Long id);

    CommentListResponseDto getCommentList(GetCommentListServiceDto getCommentListServiceDto);


}
