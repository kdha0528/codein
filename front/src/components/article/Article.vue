<template>
    <div id="content" class="content d-flex flex-column">
        <el-divider content-position="left" class="mt-4">
            <div class="text_link category"
                @click="router.replace('/'+category)"
                 style="font-size: 1rem; cursor: pointer;">
                {{article.category}}
            </div>
        </el-divider>
        <div class="member_info d-flex mt-4 justify-content-between">
            <div class="d-flex">
                <img v-if="article.imagePath" :src="article.imagePath"
                     style="width: 3rem; height: 3rem; border-radius: 100%; cursor: pointer;"
                     alt=""
                     @click="router.replace( '/members/'+article.authorId)"/>
                <el-icon v-else size="40"
                         style="width: 3rem;  height:3rem; border-radius: 100%; color:white; background-color: #E2E2E2; cursor: pointer;"
                         @click="router.replace( '/members/'+article.authorId)">
                    <Avatar/>
                </el-icon>
                <div class="d-flex flex-column justify-content-center ms-3">
                    <div class="text_link" @click="router.replace( '/members/'+article.authorId)" style="cursor: pointer; margin-bottom:0.2rem;">{{ article.nickname }}</div>
                    <div class="d-flex align-items-center align-content-lg-start" >
                        <div style="margin-right: 0.5rem;">{{article.createdAt}}</div>
                        <span style="content: '\00B7';">&#183;</span>
                        <div class="d-flex align-items-center">
                            <el-icon class="ms-2" style="width: 1.5rem;  height:1.5rem; border-radius: 100%;">
                                <View/>
                            </el-icon>
                            {{ article.viewNum }}
                        </div>
                        <div v-if="false">
                            <span style="content: '\00B7';">&#183;</span>
                            <span>수정됨</span>
                        </div>
                    </div>
                </div>
            </div>
            <div v-if="isAuthor()" class="d-flex">
                <el-dropdown  trigger="click">
                    <span class="el-dropdown-link">
                    <el-icon size="25" color="#B2B2B2" class="dropdown pt-1" style="cursor: pointer;">
                        <MoreFilled/>
                    </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu class="dropdown_menu d-flex flex-column"
                                          style="
                                        --el-dropdown-menuItem-hover-fill: white;
                                        --el-menu-hover-bg-color: white;
                                        --el-menu-hover-text-color: #409eff;
                                        --el-menu-active-color: #409eff">
                            <el-dropdown-item class="d-flex" style="padding-right: 5rem;" @click="toEditArticle()">
                                    <el-icon>
                                        <Edit />
                                    </el-icon>
                                    수정하기
                            </el-dropdown-item>
                            <el-dropdown-item class="d-flex" @click="onDeleteArticle()">
                                    <el-icon>
                                        <Delete />
                                    </el-icon>
                                    삭제하기
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </div>
        <div class="mt-5"
             style="font-size: 2.5rem;">
            {{article.title}}
        </div>
        <p class="mt-5" style="font-size: 1.1rem;" >
            {{article.content}}
        </p>
        <div class="d-flex" style="margin: 3rem auto 3rem auto;">
            <el-button class="like_button pe-3 ps-3"
                       size="large"
                       @click="onLikeArticle()"
                       :disabled="!authStore.isLoggedIn">
                <el-icon size="25">
                    <CaretTop />
                </el-icon>
                <span style="font-size: 1.2rem;">
                {{article.likeNum}}
            </span>
            </el-button>
            <el-button class="dislike_button pe-3 ps-3"
                       size="large"
                       @click="onDislikeArticle()"
                       :disabled="!authStore.isLoggedIn">
                <el-icon size="25">
                    <CaretBottom />
                </el-icon>
                <span style="font-size: 1.2rem;">
                {{article.dislikeNum}}
            </span>
            </el-button>
        </div>

        <el-divider/>
        <div class="d-flex mt-3 mb-3 align-items-center">
            <el-icon size="25">
                <ChatDotSquare/>
            </el-icon>
            <span style="font-size: 1.2rem; padding-left: 3px;">
                {{ article.commentNum }}개의 댓글
            </span>
            <div v-if="!isAuthor()" class="create-dummies-button">
                <el-button plain @click="onCreateCommentDummies()" style="border: none; padding-left: 3px; color: black;">
                    <el-icon style="vertical-align: middle">
                        <Plus />
                    </el-icon>
                </el-button>
            </div>
        </div>
        <div
            class="d-flex flex-column mb-5 p-4"
            style="border: solid thin #E2E2E2; border-radius: 5px;" >
            <div class="d-flex justify-content-between">
                <img v-if="article.imagePath" :src="article.imagePath"
                     style="width: 3rem; height: 3rem; border-radius: 100%;"
                     alt="">
                <el-icon v-else size="40"
                         style="width: 3rem;  height:3rem; border-radius: 100%; color:white; background-color: #E2E2E2;">
                    <Avatar/>
                </el-icon>
                <el-input v-model="comment.content"
                          class="comment-input ms-4"
                          size="large"
                          :autosize="{ minRows: 3, maxRows: 6 }"
                          placeholder="댓글을 입력해주세요."
                          show-word-limit
                          maxlength="3000"
                          type="textarea"
                          resize="none"
                          input-style="font-size: 1rem; line-height: 2rem; letter-spacing:0.05rem;"
                          style="max-width: 76vw;">
                </el-input>
            </div>
            <el-button class="comment-button"
                type="primary"
                @click="onWriteComment(comment)"
                size="large"
                :disabled="comment.content.length<1 || !authStore.isLoggedIn"
                style="max-width: 5rem; margin: 1.5rem 0 auto auto;">
                댓글 쓰기
            </el-button>
        </div>
        <div id="comment_list">
            <keep-alive>
                <Comments :key="commentsKey" />
            </keep-alive>
        </div>
    </div>
</template>
<script setup lang="ts">
import {inject, onMounted, provide, ref} from "vue";
import {deleteArticle, dislikeArticle, getArticle, likeArticle} from "@/controller/api/article";
import {useRoute, useRouter} from "vue-router";
import {useResponseStore} from "@/stores/Response";
import {Avatar, CaretBottom, CaretTop, ChatDotSquare, MoreFilled, UserFilled} from "@element-plus/icons-vue";
import {useAuthStore} from "@/stores/auth";
import {useCommentsStore} from "@/stores/comments";
import {usePageStore} from "@/stores/page";
import type {Comment} from "@/custom-types/comment";
import Comments from "@/components/comment/Comments.vue";
import {createCommentDummies, write} from "@/controller/api/comment";
import {isUndefined} from "element-plus/es/utils";

const route = useRoute();
const router = useRouter();
const resStore = useResponseStore();
const authStore = useAuthStore();
const commentsStore = useCommentsStore();
const pageStore = usePageStore();

const commentsKey = ref(0);

const article = ref({
    id:'',
    category:'',
    title:'',
    content:'',
    commentNum:'',
    viewNum:'',
    likeNum:'',
    dislikeNum:'',
    createdAt:'',
    authorId:'',
    nickname:'',
    imagePath:'',
    deleted:false,
})

const comment = ref({
    content: '',
})

const category = ref('');


const isSetArticlePage = ref(false);
const isSetCommentPage = ref(false);
const clickCommentPaging = ref(false);

const isAuthor = function () {
    if(authStore.isLoggedIn){
        return article.value.authorId === authStore.getId;
    } else {
        return false;
    }
}


const onPaging = async function(page: number, componentName: string) {
    if(componentName === 'Articles'){
        await pageStore.setArticlesCurrentPage(page);
        isSetArticlePage.value = true;
        await onGetArticle();
    } else if (componentName === 'Comments'){
        await pageStore.setCommentsCurrentPage(page);
        isSetCommentPage.value = true;
        clickCommentPaging.value = true;
        await onGetArticle();
    }
}

provide('onPaging', onPaging);

onMounted( async ()=>{
    await setDefaultFromPath(route.fullPath);
    await onGetArticle();
    scrollTo( 0,  0);
})

function setDefaultFromPath(path: string):void {
    const regex = /[?&]([^=]+)=([^&]+)/;

    pageStore.setCommentsCurrentPage(1);

    const match = regex.exec(path);
    if (match) {
        const paramName = match[1];
        const paramValue = match[2];
        if(paramName === "cpage") {
            pageStore.setCommentsCurrentPage(parseInt(paramValue, 10));
            isSetCommentPage.value = true;
        }
    }
}

const getPath = function(){
    let path = route.path;

    if(isSetCommentPage.value) {
        path = path + '?cpage=' + pageStore.getCommentsCurrentPage;
    }

    return path;
}

const onGetArticle = async function() {
    await getArticle(getPath())
        .then((response: any)=>{
            if(resStore.isOK){
                commentsStore.clean();
                article.value = {...response.articleData};
                response.commentsData.commentList.forEach((r: any) => {
                    const comment: Comment = {...r}
                    commentsStore.addComments(comment);
                })
                pageStore.setCommentsMaxPage(response.commentsData.maxPage)
                getKoreanCategory(article.value.category);
                commentsKey.value++;

                if(clickCommentPaging.value) {
                    clickCommentPaging.value = false;
                    router.replace({
                        path: getPath(),
                        query: { cpage: pageStore.getCommentsCurrentPage },
                        hash: "#comment_list"
                    });
                }
            } else {
                alert(resStore.getErrorMessage);
                console.log(resStore.getErrorMessage);
            }
        }).catch(error => {
            alert(error);
            console.log(error);
        })
}

provide('onGetArticle',onGetArticle);

const getKoreanCategory = function (c: string){
    switch(c){
        case 'COMMUNITY' :
            article.value.category = '커뮤니티';
            category.value = c.toLowerCase();
            break;
        case 'QUESTION' :
            article.value.category = 'Q&A';
            category.value = c.toLowerCase();
            break;
        case 'INFORMATION' :
            article.value.category = '정보공유';
            category.value = c.toLowerCase();
            break;
        case 'NOTICE' :
            article.value.category = '공지사항';
            category.value = c.toLowerCase();
            break;
        default:
            alert("존재하지 않는 카테고리");
            router.back();
    }
}

const onWriteComment = async function(c:any) {
    await write(route.path+'/comments', c)
        .then((response: any)=>{
            if(resStore.isOK){
                commentsKey.value++;
                comment.value.content='';
                onGetArticle();
            } else {
                alert(resStore.getErrorMessage);
                console.log(response);
                commentsKey.value++;
            }
        }).catch(error => {
            alert(error);
            console.log(error);
            commentsKey.value++;
        })
}

provide('onWriteComment', onWriteComment);

const onLikeArticle = async function () {
    await likeArticle(article.value.id)
        .then((response:any)=>{
            if(resStore.isOK){
                onGetArticle();
            } else {
                alert(resStore.getErrorMessage);
                console.log(response);
            }
        }).catch(error => {
            alert(error);
            console.log(error)
        })
}

const onDislikeArticle = async function () {
    await dislikeArticle(article.value.id)
        .then((response:any)=>{
            if(resStore.isOK){
                onGetArticle();
            } else {
                alert(resStore.getErrorMessage);
                console.log(response);
            }
        }).catch(error => {
            alert(error);
            console.log(error)
        })
}

const toEditArticle = function () {
    router.replace('/articles/' + article.value.id + '/edit');
}

const onDeleteArticle = async function () {
    await deleteArticle(route.path)
        .then((response:any)=>{
            if(resStore.isOK){
                alert("게시물의 삭제가 완료되었습니다.");
                router.replace({name: category.value as string});
            } else {
                alert(resStore.getErrorMessage);
                console.log(resStore.getErrorMessage);
            }
        }).catch(error => {
            alert(error);
            console.log(error)
        })
}

const onCreateCommentDummies = async function() {
    await createCommentDummies(route.path+'/dummies')
        .then(()=>{
            if(resStore.isOK){
                alert("생성이 완료되었습니다.");
                onGetArticle();
            } else {
                alert(resStore.getErrorMessage);
                console.log(resStore.getErrorMessage);
            }
        }).catch(error => {
            alert(error);
            console.log(error)
        })
}
</script>
<style scoped>
@import "../css/contentBase.css";

.category {
    color:#B2B2B2;
    font-weight: normal;
}

.text_link:hover{
    color: #409eff;
}

.like_button {
    --el-button-hover-bg-color: white;
    border:solid thin #409eff;
    color:#409eff;
}

.dislike_button {
    --el-button-hover-bg-color: white;
    border:solid thin #dc3545;
    color:#dc3545;
}

.like_button:hover, .like_button:active{
    border-color: #409eff;
    background-color: lightcyan;
    color:#409eff;
}

.dislike_button:hover, .dislike_button:active{
    color:#dc3545;
    background-color: #ffe0eb;
    border-color:#dc3545;
}


.dropdown_menu {
    .el-dropdown-item {
        font-size: 1rem;
        .el-icon{
            font-size:22px;
            width: 2.2rem;
            height: 2.2rem;
        }
    }
}

.dropdown:hover{
    color: black;
}
</style>