export interface Notification {
    id: Number,
    senderId: Number,
    senderImageUrl: String,
    senderNickname: String,
    articleId: Number,
    commentId: Number,
    subContent: String,
    content: String,
    checked: Boolean,
    clicked: Boolean,
    notifiedAt: String
}