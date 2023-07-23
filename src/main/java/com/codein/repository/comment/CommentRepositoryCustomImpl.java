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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.codein.domain.comment.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryCustomImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Comment findTargetById(Long id) {
        return jpaQueryFactory.selectFrom(comment)
                .where(comment.id.eq(id))
                .fetchOne();
    }

    @Override
    public CommentListResponseDto getCommentList(GetCommentListServiceDto getCommentListServiceDto) {

        List<Comment> parentList = jpaQueryFactory.selectFrom(comment)
                .where(comment.article.id.eq(getCommentListServiceDto.getArticleId()), comment.parentId.isNull())
                .orderBy(comment.id.asc())
                .fetch();

        List<Comment> childList = jpaQueryFactory.selectFrom(comment)
                .where(comment.article.id.eq(getCommentListServiceDto.getArticleId()), comment.parentId.isNotNull())
                .orderBy(comment.parentId.asc(), comment.id.asc())
                .fetch();

        List<Comment> orderedList = new ArrayList<>();

        int childIndex = 0;
        for (Comment parent : parentList) {
            orderedList.add(parent);
            while (childIndex < childList.size()) {
                Comment child = childList.get(childIndex);
                if (child.getParentId().equals(parent.getId())) {
                    orderedList.add(child);
                    childIndex++;
                } else {
                    break;
                }
            }
        }

        long count = orderedList.size();
        int maxPage = (int) Math.floorDiv(count, getCommentListServiceDto.getSize());
        if (count % getCommentListServiceDto.getSize() != 0) {
            maxPage++;
        }

        List<Comment> fetchResult = orderedList.subList(
                (int)Math.min(getCommentListServiceDto.getOffset(), orderedList.size()),
                (int)Math.min(getCommentListServiceDto.getOffset() + getCommentListServiceDto.getSize(), orderedList.size())
        );

        List<CommentListItem> commentList = fetchResult
                .stream()
                .map(Comment::toCommentListItem)
                .collect(Collectors.toList());

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
