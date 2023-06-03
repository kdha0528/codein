<template>
    <ul class=" d-flex flex-column" style="width:100%; margin-block:0; padding-inline-start: 0">
        <li v-for="activity in activitiesStore.getActivities" :key="activity.createdAt">
            <div class="d-flex justify-content-between mb-3">
                <div class="d-flex align-items-center">
                    <el-button plain round class="category" @click="router.replace('/'+activity.category.toLowerCase())">
                        {{ getCategory(activity.category) }}
                    </el-button>
                    <div class="ms-2">
                        <div v-if="route.name === 'members-default' || route.name === 'members-articles'">
                            <span>카테고리에서&nbsp;</span>
                            <span style="color:#409eff;">게시물</span>
                            <span>을&nbsp;작성하였습니다.</span>
                        </div>
                        <div v-else-if="route.name === 'members-comments'">
                            <span class = "nameLink" @click="router.replace('/members/'+activity.authorId)">{{activity.nickname}}</span>
                            <span>님의&nbsp;게시물에&nbsp;</span>
                            <span style="color:#409eff;">댓글</span>
                            <span>을&nbsp;달았습니다.</span>
                        </div>
                        <div v-else-if="route.name === 'members-liked-articles'">
                            <span class = "nameLink" @click = "router.replace('/members/'+activity.authorId)">{{activity.nickname}}님의&nbsp;게시물을&nbsp;</span>
                            <span style="color:#409eff;">추천</span>
                            <span>하였습니다.</span>
                        </div>
                    </div>
                </div>
                <div class="d-flex">
                    <div class="d-flex align-items-center">
                        {{ activity.createdAt }}
                    </div>
                </div>
            </div>
            <div style="font-size:1.25rem; cursor: pointer;" @click="router.replace('/articles/'+activity.id)">
                {{ activity.title }}
            </div>
            <el-divider class="mt-3 mb-3"/>
        </li>
    </ul>
    <Pagination :key="paginationKey"/>
</template>
<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {useActivitiesStore} from "@/stores/activities";
import {onMounted, ref} from "vue";
import Pagination from "@/components/pagination/Pagination.vue";

const activitiesStore = useActivitiesStore();
const router = useRouter();
const route = useRoute();
const paginationKey = ref(0);

onMounted(async () => {
    paginationKey.value++;
})
const getCategory = function (category: string) {
    switch(category){
        case "COMMUNITY":
            return "커뮤니티";
        case "QUESTION":
            return "Q&A";
        case "INFORMATION":
            return "정보공유";
        case "NOTICE":
            return "공지사항";
        default:
            break;
    }
}



</script>
<style scoped>

li {
    list-style: none;
}

.category{
    color: #409eff;
}

.nameLink {
    color: #409eff;
    cursor: pointer;
}

.nameLink:hover {
    color: skyblue;
}
</style>