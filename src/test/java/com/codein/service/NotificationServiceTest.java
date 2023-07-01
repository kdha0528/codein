package com.codein.service;

import com.codein.domain.article.Article;
import com.codein.domain.auth.Tokens;
import com.codein.domain.comment.Comment;
import com.codein.domain.member.Member;
import com.codein.domain.notification.Notification;
import com.codein.domain.notification.NotificationContent;
import com.codein.error.exception.comment.CommentNotExistsException;
import com.codein.error.exception.member.MemberNotExistsException;
import com.codein.error.exception.notification.NotificationNotExistsException;
import com.codein.repository.TokensRepository;
import com.codein.repository.article.ArticleRepository;
import com.codein.repository.comment.CommentRepository;
import com.codein.repository.member.MemberRepository;
import com.codein.repository.notification.NotificationRepository;
import com.codein.requestdto.article.NewArticleDto;
import com.codein.requestdto.comment.NewCommentDto;
import com.codein.requestdto.member.LoginDto;
import com.codein.requestdto.member.SignupDto;
import com.codein.requestservicedto.member.FollowServiceDto;
import com.codein.requestservicedto.notification.ClickNotificationServiceDto;
import com.codein.requestservicedto.notification.GetNotificationsServiceDto;
import com.codein.requestservicedto.notification.NewNotificationServiceDto;
import com.codein.responsedto.notification.NotificationListResponseDto;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseCookie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootTest
public class NotificationServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TokensRepository tokensRepository;
    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private NotificationService notificationService;

    @BeforeEach
    void signupLogin() {
        SignupDto signupDto = SignupDto.builder()
                .name("김동하")
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

    Member signup2() {
        SignupDto signupDto = SignupDto.builder()
                .name("김동하")
                .email("kdha0528@gmail.com")
                .nickname("닉네임")
                .password("12341234")
                .phone("01012341234")
                .birth("1996-05-28")
                .sex("male")
                .build();
        return memberService.signup(signupDto.toSignupServiceDto());
    }

    Cookie login2(){
        LoginDto loginDto = LoginDto.builder()
                .email("kdha0528@gmail.com")
                .password("12341234")
                .build();
        ArrayList<String> tokens = memberService.login(loginDto.toMemberServiceDto());
        return new Cookie("accesstoken",tokens.get(1));
    }

    @AfterEach
    void clean() {
        notificationRepository.deleteAll();
        commentRepository.deleteAll();
        articleRepository.deleteAll();
        memberRepository.deleteAll();
    }

    String getToken() {
        Member member = memberRepository.findByEmail("kdha4585@gmail.com");
        Tokens tokens = tokensRepository.findByMember(member)
                .orElseThrow(MemberNotExistsException::new);
        return tokens.getAccessToken();
    }
    String getToken2() {
        Member member = memberRepository.findByEmail("kdha0528@gmail.com");
        Tokens tokens = tokensRepository.findByMember(member)
                .orElseThrow(MemberNotExistsException::new);
        return tokens.getAccessToken();
    }

    Article newArticle(String accessToken) {

        NewArticleDto newArticleDto = NewArticleDto.builder()
                .category("NOTICE")
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        return articleService.newArticle(newArticleDto.toNewArticleServiceDto(), accessToken);
    }

    Comment newComment(Article article, String accessToken){
        NewCommentDto newCommentDto = NewCommentDto.builder()
                .content("댓글입니다.")
                .build();
        return commentService.newComment(newCommentDto.toNewCommentServiceDto(accessToken,article.getId()));
    }
    Comment newReply(Article article, Comment comment, String accessToken){
        NewCommentDto newCommentDto = NewCommentDto.builder()
                .targetId(comment.getId())
                .content("대댓글입니다.")
                .build();
        return commentService.newComment(newCommentDto.toNewCommentServiceDto(accessToken,article.getId()));
    }

    void createNotificationsDummies(Article article, Comment comment, String accessToken){

        Random random = new Random();
        List<Comment> comments = new ArrayList<>();

        for(int i = 0; i < 50; i ++){
            int randomIdx = random.nextInt(3);

            switch (randomIdx){
                case 0 -> {
                    NewCommentDto newCommentDto = NewCommentDto.builder()
                            .content(i+"번째 알림입니다.")
                            .build();
                    comments.add(commentService.newComment(newCommentDto.toNewCommentServiceDto(accessToken,article.getId())));
                }

                case 1 -> {
                    NewCommentDto newCommentDto = NewCommentDto.builder()
                            .targetId(comment.getId())
                            .content(i+"번째 알림입니다.")
                            .build();
                    comments.add(commentService.newComment(newCommentDto.toNewCommentServiceDto(accessToken,article.getId())));
                }

                case 2 -> {
                    if (i > 4) {
                        NewCommentDto newCommentDto = NewCommentDto.builder()
                                .targetId(comments.get(random.nextInt(5)).getId())
                                .content(i+"번째 알림입니다.")
                                .build();
                        comments.add(commentService.newComment(newCommentDto.toNewCommentServiceDto(accessToken,article.getId())));
                    } else {
                        NewCommentDto newCommentDto = NewCommentDto.builder()
                                .content(i+"번째 알림입니다.")
                                .build();
                        comments.add(commentService.newComment(newCommentDto.toNewCommentServiceDto(accessToken,article.getId())));
                    }
                }
                default -> System.out.println("error");
            }
        }

        Member sender = memberRepository.findByAccessToken(accessToken);

        comments.forEach(item -> {
            NewNotificationServiceDto newNotificationServiceDto = NewNotificationServiceDto.builder()
                    .sender(sender)
                    .article(article)
                    .comment(item)
                    .build();
            notificationService.newNotifications(newNotificationServiceDto);
        });

    }

    @Test
    @DisplayName("알림 생성 성공: 팔로잉이 새 글 작성")
    void test1_1() {
        // given
        Member sender = signup2();
        Member receiver = memberRepository.findByAccessToken(getToken());

        FollowServiceDto followServiceDto = FollowServiceDto.builder()
                .accessToken(getToken())
                .followingId(sender.getId())
                .build();
        memberService.followMember(followServiceDto);

        login2();
        Article article = newArticle(getToken2());

        NewNotificationServiceDto newNotificationServiceDto = NewNotificationServiceDto.builder()
                .sender(sender)
                .article(article)
                .build();

        // when
        notificationService.newNotifications(newNotificationServiceDto);

        // then
        List<Notification> notificationList = notificationRepository.findAllByReceiver(receiver);
        notificationList.forEach(notification -> {
            Assertions.assertEquals(notification.getArticle().getId(), article.getId());
            Assertions.assertEquals(notification.getContent(), NotificationContent.FOLLOWING_POST_NEW_ARTICLE);
            Assertions.assertEquals(notification.getSender().getId(), sender.getId());
            Assertions.assertEquals(notification.getReceiver().getId(), receiver.getId());
        });
    }

    @Test
    @DisplayName("알림 생성 성공: 댓글 작성시 글 작성자에게 알림")
    void test1_2() {
        // given
        Member receiver = memberRepository.findByAccessToken(getToken());
        Article article = newArticle(getToken());

        Member sender = signup2();
        login2();

        Comment comment = newComment(article, getToken2());

        NewNotificationServiceDto newNotificationServiceDto = NewNotificationServiceDto.builder()
                .sender(sender)
                .article(article)
                .comment(comment)
                .build();

        // when
        notificationService.newNotifications(newNotificationServiceDto);

        // then
        List<Notification> notificationList = notificationRepository.findAllByReceiver(receiver);
        notificationList.forEach(notification -> {
            Assertions.assertEquals(article.getId(), notification.getArticle().getId());
            Assertions.assertEquals( comment.getId(), notification.getComment().getId());
            Assertions.assertEquals(NotificationContent.COMMENT_ON_MY_ARTICLE, notification.getContent());
            Assertions.assertEquals(sender.getId(), notification.getSender().getId());
            Assertions.assertEquals(receiver.getId(), notification.getReceiver().getId());
        });
    }

    @Test
    @DisplayName("알림 생성 성공: 대댓글 생성시 부모댓글 작성자에게 알림")
    void test1_3() {
        // given
        Member sender = memberRepository.findByAccessToken(getToken());
        Article article = newArticle(getToken());

        Member receiver = signup2();
        login2();
        Comment comment = newComment(article, getToken2());

        Comment reply = newReply(article,comment, getToken());
        Comment reply2 = newReply(article,reply, getToken());

        NewNotificationServiceDto newNotificationServiceDto = NewNotificationServiceDto.builder()
                .sender(sender)
                .article(article)
                .comment(reply2)
                .build();

        // when
        notificationService.newNotifications(newNotificationServiceDto);

        // then
        List<Notification> notificationList = notificationRepository.findAllByReceiver(receiver);

        notificationList.forEach(notification -> {
            Assertions.assertEquals(article.getId(), notification.getArticle().getId());
            Assertions.assertEquals(reply2.getId(), notification.getComment().getId());
            Assertions.assertEquals(NotificationContent.REPLY_ON_MY_COMMENT, notification.getContent());
            Assertions.assertEquals(sender.getId(), notification.getSender().getId());
            Assertions.assertEquals(receiver.getId(), notification.getReceiver().getId());
        });
    }

    @Test
    @DisplayName("알림 생성 성공: 대댓글 생성시 부모댓글 작성자에게 알림")
    void test1_4() {
        // given
        Member receiver = memberRepository.findByAccessToken(getToken());
        Article article = newArticle(getToken());
        Comment comment = newComment(article, getToken());

        Member sender = signup2();
        login2();
        Comment reply = newReply(article,comment, getToken2());

        NewNotificationServiceDto newNotificationServiceDto = NewNotificationServiceDto.builder()
                .sender(sender)
                .article(article)
                .comment(reply)
                .build();

        // when
        notificationService.newNotifications(newNotificationServiceDto);

        // then
        List<Notification> notificationList = notificationRepository.findAllByReceiver(receiver);
        notificationList.forEach(notification -> {
            Assertions.assertEquals(article.getId(), notification.getArticle().getId());
            Assertions.assertEquals(reply.getId(), notification.getComment().getId());
            Assertions.assertEquals(NotificationContent.REPLY_TO_ME, notification.getContent());
            Assertions.assertEquals(sender.getId(), notification.getSender().getId());
            Assertions.assertEquals(receiver.getId(), notification.getReceiver().getId());
        });
    }

    @Test
    @DisplayName("알림 개수 출력 성공")
    void test2_1() {
        // given
        Member receiver = memberRepository.findByAccessToken(getToken());
        Article article = newArticle(getToken());
        Comment comment = newComment(article,getToken());

        Member sender = signup2();
        login2();

        List<Comment> comments = new ArrayList<>();

        comments.add(newComment(article,getToken2()));
        comments.add(newComment(article,getToken2()));
        comments.add(newReply(article,comment,getToken2()));
        comments.add(newReply(article,comments.get(2),getToken2()));
        comments.add(newReply(article,comments.get(3),getToken2()));
        comments.add(newReply(article,comments.get(0), getToken2()));

        // when
        comments.forEach(item -> {
            NewNotificationServiceDto newNotificationServiceDto = NewNotificationServiceDto.builder()
                    .sender(sender)
                    .article(article)
                    .comment(item)
                    .build();
            notificationService.newNotifications(newNotificationServiceDto);
        });

        ResponseCookie responseCookie = notificationService.countNewNotifications(getToken());

        // then
        Assertions.assertEquals("6",responseCookie.getValue());
    }

    @Test
    @DisplayName("알림 목록 가져오기 성공: 첫번째 일 때")
    void test3_1() {
        // given
        Article article = newArticle(getToken());
        Comment comment = newComment(article,getToken());

        Member sender = signup2();
        login2();

        createNotificationsDummies(article,comment,getToken2());

        // when
        GetNotificationsServiceDto getNotificationsServiceDto = GetNotificationsServiceDto.builder()
                .accessToken(getToken())
                .build();
        NotificationListResponseDto notificationListResponseDto = notificationService.getNotifications(getNotificationsServiceDto);

        //then
        Long commentId = notificationListResponseDto.getNotificationListItemList().get(0).getCommentId();
        Comment assertionComment = commentRepository.findById(commentId)
                        .orElseThrow(CommentNotExistsException::new);
        Assertions.assertEquals("49번째 알림입니다.",assertionComment.getContent());

        Long commentId2 = notificationListResponseDto.getNotificationListItemList().get(19).getCommentId();
        Comment assertionComment2 = commentRepository.findById(commentId2)
                .orElseThrow(CommentNotExistsException::new);
        Assertions.assertEquals("30번째 알림입니다.",assertionComment2.getContent());

        Assertions.assertEquals(notificationListResponseDto.getNotificationListItemList().size(), 20);
    }

    @Test
    @DisplayName("알림 목록 가져오기 성공: 두번째 일 때")
    void test3_2() {
        // given
        Article article = newArticle(getToken());
        Comment comment = newComment(article,getToken());

        Member sender = signup2();
        login2();

        createNotificationsDummies(article,comment,getToken2());

        GetNotificationsServiceDto getNotificationsServiceDto = GetNotificationsServiceDto.builder()
                .accessToken(getToken())
                .build();
        NotificationListResponseDto notificationListResponseDto = notificationService.getNotifications(getNotificationsServiceDto);
        Long notificationId = notificationListResponseDto.getNotificationListItemList().get(19).getId();

        // when
        GetNotificationsServiceDto getNotificationsServiceDto2 = GetNotificationsServiceDto.builder()
                .lastNotificationId(notificationId)
                .accessToken(getToken())
                .build();
        NotificationListResponseDto notificationListResponseDto2 = notificationService.getNotifications(getNotificationsServiceDto2);

        //then
        Long commentId = notificationListResponseDto2.getNotificationListItemList().get(0).getCommentId();
        Comment assertionComment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotExistsException::new);
        Assertions.assertEquals("29번째 알림입니다.",assertionComment.getContent());

        Long commentId2 = notificationListResponseDto2.getNotificationListItemList().get(19).getCommentId();
        Comment assertionComment2 = commentRepository.findById(commentId2)
                .orElseThrow(CommentNotExistsException::new);
        Assertions.assertEquals("10번째 알림입니다.",assertionComment2.getContent());

        Assertions.assertEquals(notificationListResponseDto2.getNotificationListItemList().size(), 20);
    }

    @Test
    @DisplayName("알림 목록 가져오기 성공: 마지막 일 때")
    void test3_3() {
        // given
        Member receiver = memberRepository.findByAccessToken(getToken());
        Article article = newArticle(getToken());
        Comment comment = newComment(article,getToken());

        Member sender = signup2();
        login2();

        createNotificationsDummies(article,comment,getToken2());
        List<Notification> notifications = notificationRepository.findAllByReceiver(receiver);

        GetNotificationsServiceDto getNotificationsServiceDto = GetNotificationsServiceDto.builder()
                .accessToken(getToken())
                .build();
        NotificationListResponseDto notificationListResponseDto = notificationService.getNotifications(getNotificationsServiceDto);
        Long notificationId = notificationListResponseDto.getNotificationListItemList().get(19).getId();

        GetNotificationsServiceDto getNotificationsServiceDto2 = GetNotificationsServiceDto.builder()
                .lastNotificationId(notificationId)
                .accessToken(getToken())
                .build();
        NotificationListResponseDto notificationListResponseDto2 = notificationService.getNotifications(getNotificationsServiceDto2);
        Long notificationId2 = notificationListResponseDto2.getNotificationListItemList().get(19).getId();

        // when
        GetNotificationsServiceDto getNotificationsServiceDto3 = GetNotificationsServiceDto.builder()
                .lastNotificationId(notificationId2)
                .accessToken(getToken())
                .build();
        NotificationListResponseDto notificationListResponseDto3 = notificationService.getNotifications(getNotificationsServiceDto3);

        //then
        Long commentId = notificationListResponseDto3.getNotificationListItemList().get(0).getCommentId();
        Comment assertionComment = commentRepository.findById(commentId)
                .orElseThrow(CommentNotExistsException::new);
        Assertions.assertEquals("9번째 알림입니다.",assertionComment.getContent());

        Long commentId2 = notificationListResponseDto3.getNotificationListItemList().get(9).getCommentId();
        Comment assertionComment2 = commentRepository.findById(commentId2)
                .orElseThrow(CommentNotExistsException::new);
        Assertions.assertEquals("0번째 알림입니다.",assertionComment2.getContent());

        Assertions.assertEquals(10, notificationListResponseDto3.getNotificationListItemList().size());
    }

    @Test
    @DisplayName("알림 확인 후 알림 개수 초기화 성공")
    void test4_1() {
        //given
        Member receiver = memberRepository.findByAccessToken(getToken());
        Article article = newArticle(getToken());
        Comment comment = newComment(article,getToken());

        Member sender = signup2();
        login2();

        createNotificationsDummies(article,comment,getToken2());

        //when
        notificationService.checkNotifications(receiver);

        //then
        ResponseCookie responseCookie = notificationService.countNewNotifications(getToken());
        Assertions.assertNull(responseCookie);
    }

    @Test
    @DisplayName("알림 클릭 성공")
    void test5_1() {
        //given
        Member receiver = memberRepository.findByAccessToken(getToken());
        Article article = newArticle(getToken());
        Comment comment = newComment(article,getToken());

        Member sender = signup2();
        login2();

        createNotificationsDummies(article,comment,getToken2());

        List<Notification> notificationList = notificationRepository.findAllByReceiver(receiver);

        Random random = new Random();
        Notification notification = notificationList.get(random.nextInt(50));

        ClickNotificationServiceDto clickNotificationServiceDto = ClickNotificationServiceDto.builder()
                .notificationId(notification.getId())
                .accessToken(getToken())
                .build();

        //when
        notificationService.clickNotification(clickNotificationServiceDto);

        //then
        Notification clickedNotification = notificationRepository.findById(notification.getId())
                .orElseThrow(NotificationNotExistsException::new);
        Assertions.assertFalse(notification.isClicked());
        Assertions.assertTrue(clickedNotification.isClicked());
    }
}
