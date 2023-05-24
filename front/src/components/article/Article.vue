<template>
    <div class="content d-flex flex-column">
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
                    <div class="d-flex align-content-lg-start" >
                        <div style="margin-right: 0.5rem;">{{article.createdAt}}</div>
                        <span style="content: '\00B7';">&#183;</span>
                        <div style="margin-left: 0.5rem;">{{ article.viewNum }}</div>
                    </div>
                </div>
            </div>
            <div v-if="isAuthor()" class="d-flex">
                <el-dropdown  trigger="click">
                    <span class="el-dropdown-link">
                    <el-icon size="25" color="#B2B2B2" class="dropdown" style="cursor: pointer">
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
        <div class="mt-5" style="white-space: pre-line; line-height: 2rem;">
            {{article.content}}
        </div>
        <el-button class="like_button pe-3 ps-3"
                   size="large"
                   @click="onLikeArticle()">
            <el-icon size="25"
                     style="cursor: pointer;">
                <Star />
            </el-icon>
            <span style="font-size: 1.2rem;">
                {{article.likeNum}}
            </span>
        </el-button>
        <div>댓글</div>
    </div>
</template>
<script setup lang="ts">
import {onMounted, ref} from "vue";
import {deleteArticle, getArticle, likeArticle} from "@/controller/api/article";
import {useRoute, useRouter} from "vue-router";
import {useResponseStore} from "@/stores/Response";
import {Avatar, MoreFilled} from "@element-plus/icons-vue";
import {useAuthStore} from "@/stores/auth";

const route = useRoute();
const router = useRouter();
const resStore = useResponseStore();
const authStore = useAuthStore();

const article = ref({
    id:'',
    category:'',
    title:'',
    content:'',
    commentNum:'',
    viewNum:'',
    likeNum:'',
    createdAt:'',
    authorId:'',
    nickname:'',
    imagePath:'',
    deleted:false,
})

const category = ref('');

const isAuthor = function () {
    return article.value.authorId === authStore.getId;
}

onMounted( ()=>{
    onGetArticle();
})

const onGetArticle = async function() {
    await getArticle(route.path)
        .then((response: any)=>{
            if(resStore.isOK){
                if(response.deleted) {
                    alert("삭제된 게시물입니다.");
                    router.go(-1);
                } else {
                    article.value = {...response};
                    getKoreanCategory(response.category);
                }
            } else {
                alert(resStore.getErrorMessage);
                console.log(response)
            }
        }).catch(error => {
            alert(error);
            console.log(error)
        })
}

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

const onLikeArticle = async function () {
    await likeArticle(article.value.id)
        .then((response:any)=>{
            if(resStore.isOK){
                alert("추천이 완료되었습니다.");
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
                console.log(response);
            }
        }).catch(error => {
            alert(error);
            console.log(error)
        })
}
</script>
<style scoped>
@import "../css/contentBase.css";

li {
    list-style: none;
}

.category {
    color:#B2B2B2;
    font-weight: normal;
}

.text_link:hover{
    color: #409eff;
}

.like_button {
    --el-button-hover-bg-color: white;
    border:solid 0.1rem #b1b3b8;
    margin: 3rem auto 3rem auto;
}

.like_button:hover{
    border: solid 0.1rem #409eff;
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
    color: #409eff;
}
</style>