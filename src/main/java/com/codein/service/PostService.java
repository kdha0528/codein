package com.codein.service;

import com.codein.domain.Member;
import com.codein.domain.Session;
import com.codein.error.exception.member.MemberNotLoginException;
import com.codein.repository.SessionRepository;
import com.codein.repository.post.PostRepository;
import com.codein.requestservicedto.PostingServiceDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public void post(PostingServiceDto postingServiceDto, String token) {
        Session session = sessionRepository.findByAccessToken(token)
                .orElseThrow(MemberNotLoginException::new);
        Member member = session.getMember();
        postRepository.save(postingServiceDto.toEntity(member));
    }
}
