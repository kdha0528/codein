package com.codein.service;

import com.codein.domain.article.Article;
import com.codein.domain.comment.CommentLike;
import com.codein.repository.comment.like.CommentLikeRepository;
import com.codein.utils.LikeChanges;
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
import java.util.Random;

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
    public Comment newComment(NewCommentServiceDto newCommentServiceDto) {
        Article article = articleRepository.findById(newCommentServiceDto.getArticleId())
                .orElseThrow(ArticleNotExistsException::new);
        Member member = memberRepository.findByAccessToken(newCommentServiceDto.getAccessToken());

        if(member != null) {
            Comment target = null;
            String content = newCommentServiceDto.getContent();
            String targetNickname = null;

            if(newCommentServiceDto.getTargetId() != null) {    // target이 있는지 확인
                target = commentRepository.findTargetById(newCommentServiceDto.getTargetId());  // target id 유효하면 target 추가
                String targetMentionNickname = newCommentServiceDto.hasTarget(); // null or @+targetNickname
                if(targetMentionNickname != null) {
                    targetNickname = targetMentionNickname.replace("@","");
                    if(targetNickname.equals(target.getMember().getNickname())){ // @를 제외한 진짜 nickname이 target id로 찾은 target의 nickname과 같다면
                        content = newCommentServiceDto.getContent().replace(targetMentionNickname,"");  // content에서 metion nickname을 제거하고 저장하고 target nickname 저자
                    } else {
                        targetNickname = null; // 일치하지 않는다면 target nickname을 null로 변환
                    }
                }
            }

            Comment newComment = Comment.builder()
                    .parentId(newCommentServiceDto.getParentId())
                    .member(member)
                    .article(article)
                    .target(target)
                    .targetNickname(targetNickname)
                    .content(content)
                    .build();

            commentRepository.save(newComment);
            article.increaseCommentNum();
            return newComment;
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
                            .isLike(true)
                            .build();
                    commentLikeRepository.save(commentLike);
                    comment.changeLikeNum(1);
                } else {
                    CommentLike commentLike = CommentLike.builder()
                            .comment(comment)
                            .member(member)
                            .isLike(false)
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

    @Transactional
    public void createCommentDummies(Article article, Member member) {
        Random random = new Random();
        for(int i = 0; i < 20; i++){
            Comment comment = Comment.builder()
                    .content("댓글 테스트 "+i)
                    .article(article)
                    .member(member)
                    .build();
            commentRepository.save(comment);

            for(int j = 0; j < random.nextInt(8);j++){
                Comment child = Comment.builder()
                        .parentId(comment.getId())
                        .content(i+"의 "+j+"번 째 자식 댓글")
                        .article(article)
                        .member(member)
                        .build();
                commentRepository.save(child);
            }
        }
    }
}
