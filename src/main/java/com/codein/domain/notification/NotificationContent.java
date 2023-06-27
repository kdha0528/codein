package com.codein.domain.notification;

public enum NotificationContent {

    FOLLOWING_POST_NEW_ARTICLE("팔로잉이 새 글 작성"),
    COMMENT_ON_MY_ARTICLE("내 글에 새 댓글 생성"),
    REPLY_ON_MY_COMMENT("내 댓글에 새 대댓글 생성"),
    REPLY_TO_ME("나를 멘션으로 하는 새 대댓글 생성");

    private final String notificationContent;

    NotificationContent(String notificationContent) {this.notificationContent = notificationContent;}

    public String getValue(){return notificationContent;}

    public String getName() { return this.name(); }

}
