package com.codein.service;

import com.codein.domain.Member;
import com.codein.domain.Post;
import com.codein.domain.Session;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.SessionRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.repository.post.PostRepository;
import com.codein.requestdto.LoginDto;
import com.codein.requestdto.PostingDto;
import com.codein.requestdto.SignupDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;

    @BeforeEach
    void signupLogin() {
        SignupDto signupDto = SignupDto.builder()
                .name("데일이")
                .email("kdha4585@gmail.com")
                .nickname("데일이")
                .password("12341234")
                .phone("01075444357")
                .birth("1996-05-28")
                .sex("male")
                .build();
        memberService.signup(signupDto.toSignupServiceDto());

        LoginDto loginDto = LoginDto.builder()
                .email("kdha4585@gmail.com")
                .password("12341234")
                .build();
        memberService.login(loginDto.toMemberServiceDto());
    }

    @AfterEach
    void clean() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
    }

    String getToken() {
        Member member = memberRepository.findByEmail("kdha4585@gmail.com");
        Session session = sessionRepository.findByMember(member)
                .orElseThrow(MemberNotExistsException::new);
        return session.getAccessToken();
    }

    @Test
    @DisplayName("글 등록 성공")
    void test1() {
        // given
        String accessToken = getToken();
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