package com.codein.service;

import com.codein.domain.Session;
import com.codein.domain.member.Member;
import com.codein.domain.post.Post;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.repository.SessionRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.repository.post.PostRepository;
import com.codein.requestdto.member.LoginDto;
import com.codein.requestdto.member.SignupDto;
import com.codein.requestdto.post.EditPostDto;
import com.codein.requestdto.post.WritePostDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        WritePostDto writePostDto = WritePostDto.builder()
                .category("NOTICE")
                .title("제목")
                .content("내용")
                .build();
        // when
        postService.writePost(writePostDto.toWritePostServiceDto(), accessToken);

        //then
        Member member = memberRepository.findByAccessToken(accessToken);
        List<Post> posts = postRepository.findByMember(member);
        Post post = posts.get(0);

        Assertions.assertEquals(writePostDto.getCategory(), post.getCategory().getValue());
        Assertions.assertEquals(writePostDto.getTitle(), post.getTitle());
        Assertions.assertEquals(writePostDto.getContent(), post.getContent());
    }

    @Test
    @DisplayName("글 수정 성공")
    void test2() {
        // given
        String accessToken = getToken();

        WritePostDto writePostDto = WritePostDto.builder()
                .category("NOTICE")
                .title("제목")
                .content("내용")
                .build();
        postService.writePost(writePostDto.toWritePostServiceDto(), accessToken);

        Member member = memberRepository.findByAccessToken(accessToken);
        List<Post> posts = postRepository.findByMember(member);
        Post post = posts.get(0);

        EditPostDto editPostDto = EditPostDto.builder()
                .category("COMMUNITY")
                .title("타이틀")
                .content("내용")
                .build();
        // when
        postService.editPost(post.getId(), editPostDto.toEditPostServiceDto());

        //then
        List<Post> editedPosts = postRepository.findByMember(member);
        Post editedPost = editedPosts.get(0);

        Assertions.assertEquals(editPostDto.getCategory(), editedPost.getCategory().getValue());
        Assertions.assertEquals(editPostDto.getTitle(), editedPost.getTitle());
        Assertions.assertEquals(editPostDto.getContent(), editedPost.getContent());
    }

}