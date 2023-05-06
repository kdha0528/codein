<template>
    <ul class="d-flex flex-column" style="width:100%; margin-block:0; padding-inline-start: 0">
        <li v-for="article in articles" :key="article.id">
            <div class="d-flex justify-content-between mb-3">
                <div class="d-flex align-items-center">
                    <img v-if="article.imagePath" :src="article.imagePath"
                         style="width: 1.8rem; height: 1.8rem; border-radius: 100%;"
                         alt=""/>
                    <el-icon v-else size="40"
                             style="width: 1.8rem;  height:1.8rem; border-radius: 100%; color:white; background-color: #E2E2E2;">
                        <Avatar/>
                    </el-icon>
                    <div class="ms-2">{{ article.nickname }}</div>
                    <div class="ms-2">{{ article.createdAt }}</div>
                </div>
                <div class="d-flex">
                    <div class="d-flex align-items-center">
                        <el-icon class="ms-2" style="width: 1.5rem;  height:1.5rem; border-radius: 100%;">
                            <View/>
                        </el-icon>
                        {{ article.viewNum }}
                    </div>
                    <div  class="d-flex align-items-center">
                        <el-icon class="ms-2" style="width: 1.5rem;  height:1.5rem; border-radius: 100%;">
                            <ChatDotSquare/>
                        </el-icon>
                        {{ article.commentNum }}
                    </div>
                    <div  class="d-flex align-items-center">
                        <el-icon class="ms-2" style="width: 1.5rem;  height:1.5rem; border-radius: 100%;">
                            <Star/>
                        </el-icon>
                        {{ article.likeNum }}
                    </div>
                </div>
            </div>
            <div style="font-size:1.25rem;">
                {{ article.title }}
            </div>
            <el-divider class="mt-3 mb-3"/>
        </li>
    </ul>
</template>
<script setup lang="ts">
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {getArticles} from "@/api/article";
import {useResponseStore} from "@/stores/Response";
import {ChatDotSquare, Star} from "@element-plus/icons-vue";

const router = useRouter();
const route = useRoute();
const resStore = useResponseStore();

const articles: any = ref([])

onMounted(()=>{
    onGetArticles();
})

const onGetArticles = async function(){
    await getArticles(route.path)
        .then((response: any)=>{
            if(resStore.isOK){
                response.forEach((r: any) => {
                   articles.value.push(r);
                });
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