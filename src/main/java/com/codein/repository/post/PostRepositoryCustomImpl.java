package com.codein.repository.post;

import com.codein.domain.Member;
import com.codein.domain.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.codein.domain.QPost.post;


@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Post findByMember(Member member) {
        return jpaQueryFactory.selectFrom(post)
                .where(post.member.eq(member))
                .fetchOne();
    }
}
