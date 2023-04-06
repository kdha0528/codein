package com.codein.service;

import com.codein.domain.Member;
import com.codein.domain.Post;
import com.codein.repository.member.MemberRepository;
import com.codein.repository.post.PostRepository;
import com.codein.requestdto.LoginDto;
import com.codein.requestdto.PostingDto;
import com.codein.requestdto.SignupDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 등록 성공")
    void test1() {
        // given
        SignupDto signupDto = SignupDto.builder()
                .name("김동하")
                .nickname("데일이")
                .email("kdha4585@gmail.com")
                .password("12341234")
                .birth("2000-01-01")
                .sex("male")
                .phone("01012341234")
                .build();
        memberService.signup(signupDto.toSignupServiceDto());

        LoginDto login = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        String accessToken = memberService.login(login.toMemberServiceDto());

        PostingDto postingDto = PostingDto.builder()
                .category("NOTICE")
                .title("제목")
                .content("내용")
                .build();
        // when
        postService.post(postingDto.toPostingServiceDto(), accessToken);

        //then
        Member member = memberRepository.findByAccessToken(accessToken);
        Post post = postRepository.findByMember(member);

        Assertions.assertEquals(postingDto.getCategory(), post.getCategory().getValue());
        Assertions.assertEquals(postingDto.getTitle(), post.getTitle());
        Assertions.assertEquals(postingDto.getContent(), post.getContent());
    }

}