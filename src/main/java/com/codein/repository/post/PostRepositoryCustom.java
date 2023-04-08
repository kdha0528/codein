package com.codein.repository.post;

import com.codein.domain.member.Member;
import com.codein.domain.post.Post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> findByMember(Member member);

}
