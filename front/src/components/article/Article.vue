<template>
  <div>카테고리</div>
  <div>
      <div>이미지</div>
      <div>닉네임</div>
      <div>작성일</div>
      <div>조회수</div>
  </div>
  <div>제목</div>
  <div>
      내용
  </div>
  <div>추천버튼</div>
  <div>댓글</div>

</template>
<script setup lang="ts">
import {onMounted, ref} from "vue";
import {getArticle} from "@/controller/api/article";
import {useRoute, useRouter} from "vue-router";
import {useResponseStore} from "@/stores/Response";

const route = useRoute();
const router = useRouter();
const resStore = useResponseStore();

const article = ref({
    id:0,
    category:'',
    title:'',
    content:'',
    createdAt:'',
    viewNum:'',
    likeNum:'',
    commentNum:'',
    authorId:0,
})

onMounted( ()=>{
    onGetArticle();
})

const onGetArticle = async function() {
    await getArticle(route.path)
        .then((response: any)=>{
            if(resStore.isOK){
                if(response.deleted === true) {
                    alert("삭제된 게시물입니다.");
                    router.go(-1);
                } else {
                    article.value = {...response};
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
</script>
<style scoped>

li {
    list-style: none;
}
</style>