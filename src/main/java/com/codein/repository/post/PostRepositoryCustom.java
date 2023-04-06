package com.codein.repository.post;

import com.codein.domain.Member;
import com.codein.domain.Post;

public interface PostRepositoryCustom {

    Post findByMember(Member member);
}
