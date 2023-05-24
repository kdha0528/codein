<template>
  <div class="content">
      <el-form
          status-icon
          label-position="top"
          label-width="120px"
          method="post"
          class="demo-ruleForm">
          <el-form-item label="카테고리" prop="category">
              <el-select v-model="select.category" size="large" style="width:100%;">
                  <el-option v-for="item in selectList.category" :label="item.text" :value="item.value" />
              </el-select>
          </el-form-item>
          <el-form-item label="제목" prop="title">
              <el-input v-model="form.title"
                        size="large"
                        maxlength="30"
                        placeholder="Please input"
                        show-word-limit
                        style="font-size:1rem;"/>
          </el-form-item>
          <el-form-item label="내용" prop="content">
              <el-input v-model="form.content"
                        size="large"
                        :autosize="{ minRows: 16 }"
                        placeholder="Please input"
                        show-word-limit
                        maxlength="5000"
                        type="textarea"
                        style="font-size:1rem;"/>
          </el-form-item>
          <el-form-item >
              <el-button type="info" @click="onCancel()" style="margin: auto 0 auto auto;">취소</el-button>
              <el-button type="primary" @click="onSubmit()" style="margin: auto 0 auto 1rem;">등록</el-button>
          </el-form-item>
      </el-form>
  </div>
</template>
<script setup lang="ts">
import {onMounted, reactive, ref} from 'vue';
import { useAuthStore } from "@/stores/auth";
import { useRoute, useRouter } from "vue-router";
import {useResponseStore} from "@/stores/Response";
import {editArticle, getArticle, write} from "@/controller/api/article";

const auth = useAuthStore();
const router = useRouter();
const route = useRoute();
const resStore = useResponseStore();

const id = ref({
    article: null as null | any,
})

const select = ref({
    category: 'COMMUNITY',
})

const selectList = reactive({
    category: [
        {
            text: "커뮤니티",
            value: "COMMUNITY",
        },
        {
            text: "Q&A",
            value: "QUESTION",
        },
        {
            text: "정보공유",
            value: "INFORMATION",
        }
    ],
})

const form = ref({
    category:'' as null | any,
    title:'' as null | any,
    content:'' as null | any,
})

onMounted(()=>{
    if(route.params.category){
        select.value.category = (route.params.category as string).toUpperCase();
    } else if(route.params.id){
        id.value.article = route.params.id;
        onGetArticle()
    }
})

const onGetArticle = async function() {
    await getArticle("/articles/"+route.params.id)
        .then((response: any)=>{
            if(resStore.isOK){
                form.value.category = response.category;
                form.value.title = response.title;
                form.value.content = response.content;
                select.value.category = response.category;
            } else {
                alert(resStore.getErrorMessage);
                console.log(response)
            }
        }).catch(error => {
            alert(error);
            console.log(error)
        })
}

const onSubmit = async function(){
    form.value.category = select.value.category;
    if(id.value.article){   // 글 수정
        await editArticle(route.path,form.value)
            .then((response: any)=> {
                if(resStore.isOK){
                    alert("수정이 완료되었습니다.")
                    router.replace({name:form.value.category.toString().toLowerCase()});
                } else {
                    alert(resStore.getErrorMessage);
                    console.log(response)
                    router.replace({name:form.value.category.toString().toLowerCase()});
                }
            }).catch(error => {
                alert(error);
                console.log(error)
                router.replace({name:form.value.category.toString().toLowerCase()});
            })
    } else { //글 생성
        await write(form.value)
            .then((response: any)=>{
                if(resStore.isOK){
                    alert("등록이 완료되었습니다.");
                    router.replace({name: route.params.category as string});
                } else {
                    alert(resStore.getErrorMessage);
                    console.log(response);
                    router.replace('/'+ route.params.category+'/new');
                }
            }).catch(error => {
                alert(error);
                console.log(error);
                router.replace('/'+ route.params.category+'/new');
            })
    }
}

const onCancel = function() {
    if(id.value.article){   // 글 수정
        alert("수정을 취소하였습니다.");
        router.back();
    } else {
        alert("등록을 취소하였습니다.");
        router.back();
    }
}
</script>
<style scoped>
@import "../css/contentBase.css";
</style>