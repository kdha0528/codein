<template>
    <ul class=" d-flex flex-column" style="width:100%; margin-block:0; padding-inline-start: 0">
        <li v-for="article in articlesStore.getArticles" :key="article.id">
            <div class="d-flex justify-content-between mb-3">
                <div class="d-flex align-items-center">
                    <img v-if="article.imagePath" :src="article.imagePath"
                         style="width: 2rem; height: 2rem; border-radius: 100%; cursor: pointer;"
                         alt=""
                         @click="router.replace( '/members/'+article.authorId)"/>
                    <el-icon v-else size="40"
                             style="width: 2rem;  height:2rem; border-radius: 100%; color:white; background-color: #E2E2E2; cursor: pointer;"
                             @click="router.replace( '/members/'+article.authorId)">
                        <Avatar/>
                    </el-icon>
                    <div class="ms-2"  @click="router.replace( '/members/'+article.authorId)" style="cursor: pointer; font-size:1rem;">{{ article.nickname }}</div>
                    <div class="ms-2"  style="font-size:1rem;">{{ article.createdAt }}</div>
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
            <div style="font-size:1.25rem; cursor: pointer;" @click="router.replace('/articles/'+article.id)">
                {{ article.title }}
            </div>
            <el-divider class="mt-3 mb-3"/>
        </li>
    </ul>
    <Pagination :key="paginationKey"/>
</template>
<script setup lang="ts">
import {ChatDotSquare, Star} from "@element-plus/icons-vue";
import {useArticlesStore} from "@/stores/articles";
import {useRoute, useRouter} from "vue-router";
import Pagination from "@/components/pagination/Pagination.vue";
import {onMounted, ref} from "vue";

const articlesStore = useArticlesStore();
const router = useRouter();
const route = useRoute();
const paginationKey = ref(0);

onMounted(async () => {
    paginationKey.value++;
})

</script>
<style scoped>

li {
    list-style: none;
}
</style>