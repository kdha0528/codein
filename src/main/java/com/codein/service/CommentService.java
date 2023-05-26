package com.codein.service;

import com.codein.domain.article.Article;
import com.codein.domain.comment.CommentLike;
import com.codein.repository.comment.like.CommentLikeRepository;
import com.codein.domain.utils.LikeChanges;
import com.codein.domain.comment.Comment;
import com.codein.domain.comment.CommentEditor;
import com.codein.domain.member.Member;
import com.codein.error.exception.article.ArticleNotExistsException;
import com.codein.error.exception.article.FrequentLikeException;
import com.codein.error.exception.article.InvalidAuthorException;
import com.codein.error.exception.comment.CommentNotExistsException;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.comment.CommentRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.requestdto.comment.GetCommentListServiceDto;
import com.codein.requestservicedto.comment.CommentLikeServiceDto;
import com.codein.requestservicedto.comment.EditCommentServiceDto;
import com.codein.requestservicedto.comment.NewCommentServiceDto;
import com.codein.responsedto.comment.CommentListResponseDto;
import com.codein.responsedto.comment.DeleteCommentServiceDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final MemberRepository memberRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;

    @Transactional
    public CommentListResponseDto getCommentList(GetCommentListServiceDto getCommentListServiceDto) {
        return commentRepository.getCommentList(getCommentListServiceDto);
    }

    @Transactional
    public void newComment(NewCommentServiceDto newCommentServiceDto) {

        Article article = articleRepository.findById(newCommentServiceDto.getArticleId())
                .orElseThrow(ArticleNotExistsException::new);

        Member member = memberRepository.findByAccessToken(newCommentServiceDto.getAccessToken());

        if(member != null){
            Comment target = commentRepository.findTargetById(newCommentServiceDto.getTargetId());
            Comment newComment = newCommentServiceDto.toEntity(member,article, target);
            commentRepository.save(newComment);
        } else {
            throw new MemberNotExistsException();
        }
    }

    @Transactional
    public void likeComment(CommentLikeServiceDto commentLikeServiceDto) {
        Comment comment = commentRepository.findById(commentLikeServiceDto.getCommentId())
                .orElseThrow(ArticleNotExistsException::new);

        Member member = memberRepository.findByAccessToken(commentLikeServiceDto.getAccessToken());

        if (member != null) {
            CommentLike exists = commentLikeRepository.existsCommentLike(comment, member);

            if (exists == null) {   // 첫 추천 혹은 비추천
                if (commentLikeServiceDto.isLike()) {
                    CommentLike commentLike = CommentLike.builder()
                            .comment(comment)
                            .member(member)
                            .like(true)
                            .build();
                    commentLikeRepository.save(commentLike);
                    comment.changeLikeNum(1);
                } else {
                    CommentLike commentLike = CommentLike.builder()
                            .comment(comment)
                            .member(member)
                            .like(false)
                            .build();
                    commentLikeRepository.save(commentLike);
                    comment.changeDislikeNum(1);
                }
            } else { // 이미 추천 기록이 있다면 업데이트
                if (LocalDateTime.now().minus(10, ChronoUnit.SECONDS).isAfter(exists.getLikedAt())) {
                    LikeChanges changes = exists.change(commentLikeServiceDto.isLike());
                    comment.changeLikeNum(changes.getLike());
                    comment.changeDislikeNum(changes.getDislike());
                } else {  // 10초에 1번씩만 업데이트 가능
                    throw new FrequentLikeException();
                }
            }
        } else {
            throw new MemberNotExistsException();
        }
    }

    @Transactional
    public Comment editComment(EditCommentServiceDto editCommentServiceDto) {
        Comment comment = commentRepository.findById(editCommentServiceDto.getId())
                .orElseThrow(CommentNotExistsException::new);

        Member member = memberRepository.findByAccessToken(editCommentServiceDto.getAccessToken());

        if(comment.getMember() == member) {

            CommentEditor commentEditor = CommentEditor.builder()
                    .content(editCommentServiceDto.getContent())
                    .build();

            return comment.edit(commentEditor);
        }else{
            throw new InvalidAuthorException();
        }
    }

    @Transactional
    public void deleteComment(DeleteCommentServiceDto deleteCommentServiceDto) {

        Comment comment = commentRepository.findById(deleteCommentServiceDto.getId())
                .orElseThrow(CommentNotExistsException::new);

        Member member = memberRepository.findByAccessToken(deleteCommentServiceDto.getAccessToken());

        if(comment.getMember() == member){
            comment.deleteComment();
        } else if (member == null){
            throw new MemberNotExistsException();
        } else {
            throw new InvalidAuthorException();
        }

    }
}
