package com.codein.repository.comment;

import com.codein.domain.article.Article;
import com.codein.domain.comment.Comment;
import com.codein.requestdto.comment.GetCommentListServiceDto;
import com.codein.responsedto.comment.CommentListItem;
import com.codein.responsedto.comment.CommentListResponseDto;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codein.domain.comment.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Comment findTargetById(Long id) {
        return jpaQueryFactory.selectFrom(comment)
                .where(comment.id.eq(id)
                        , comment.deleted.isFalse())
                .fetchOne();
    }

    @Override
    public CommentListResponseDto getCommentList(GetCommentListServiceDto getCommentListServiceDto) {

        JPAQuery<Comment> query = jpaQueryFactory.selectFrom(comment)
                .where(comment.article.eq(getCommentListServiceDto.getArticle()));

        long count = query.fetch().size();
        int maxPage = (int) Math.floorDiv(count, getCommentListServiceDto.getSize());

        List<Comment> fetchResult = query
                .orderBy(comment.parentId.desc(), comment.id.desc())
                .limit(getCommentListServiceDto.getSize())
                .offset(getCommentListServiceDto.getOffset())
                .fetch();

        List<CommentListItem> commentList = fetchResult
                .stream()
                .map(Comment::toCommentListItem)
                .toList();

        return CommentListResponseDto.builder()
                .commentList(commentList)
                .maxPage(maxPage)
                .build();
    }

    @Override
    public List<Comment> findCommentsByArticle(Article article) {
        return jpaQueryFactory.selectFrom(comment)
                .where(comment.article.eq(article))
                .orderBy(comment.id.desc())
                .fetch();
    }
}
