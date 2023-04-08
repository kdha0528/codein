package com.codein.repository.post;

import com.codein.domain.member.Member;
import com.codein.domain.post.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.codein.domain.post.QPost.post;


@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Post> findByMember(Member member) {
        return jpaQueryFactory.selectFrom(post)
                .where(post.member.eq(member))
                .fetch();
    }
}
