package com.codein.service;

import com.codein.domain.Session;
import com.codein.domain.member.Member;
import com.codein.domain.post.Post;
import com.codein.domain.post.PostEditor;
import com.codein.error.exception.member.MemberNotLoginException;
import com.codein.error.exception.post.PostNotExistsException;
import com.codein.repository.SessionRepository;
import com.codein.repository.post.PostRepository;
import com.codein.requestservicedto.post.EditPostServiceDto;
import com.codein.requestservicedto.post.WritePostServiceDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final SessionRepository sessionRepository;

    @Transactional
    public void writePost(WritePostServiceDto writePostServiceDto, String accesstoken) {
        Session session = sessionRepository.findByAccessToken(accesstoken)
                .orElseThrow(MemberNotLoginException::new);
        Member member = session.getMember();
        postRepository.save(writePostServiceDto.toEntity(member));
    }

    @Transactional
    public void editPost(Long postId, EditPostServiceDto editPostServiceDto) {
        PostEditor postEditor = PostEditor.builder()
                .category(editPostServiceDto.getCategory())
                .title(editPostServiceDto.getTitle())
                .content(editPostServiceDto.getContent())
                .build();

        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotExistsException::new);

        post.edit(postEditor);

        // 테스트 코드 짜기, post list 읽어오는 함수도 만들기

    }
}
