export interface Comment {
    id: Number,
    parentId: Number,
    content: String,
    createdAt: String,
    likeNum: Number,
    dislikeNum: Number,

    commenterId: Number,
    commenterNickname: String,
    commenterImagePath: String,

    targetMemberId: Number,
    targetNickname: String,

    deleted: boolean,
    reply: boolean,
}