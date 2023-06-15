<template>
    <div class="content">
        <div class="d-flex flex-column mb-4" style="border: thin solid #dcdfe6; border-radius: 10px;">
            <div class="d-flex align-items-center">
                <div class="ms-3 mt-4 mb-4">
                    <img v-if="memberInfo.imageUrl" :src="memberInfo.imageUrl"
                         style="width: 4.5rem; height: 4.5rem; border-radius: 100%;"
                         alt=""/>
                    <el-icon v-else size="77" class="ms-2"
                             style="width: 4.5rem;  height:4.5rem; border-radius: 100%; color:white; background-color: #E2E2E2;">
                        <Avatar/>
                    </el-icon>
                </div>
                <div class="simple-profile ms-3 d-flex flex-column">
                    <div style="font-size: 1.5rem; font-weight: bold;">{{ memberInfo.nickname }}</div>
                    <div class="d-flex align-items-center">
                        <div style="font-size: 0.9rem;">팔로우 팔로워</div>
                        <div v-if="memberInfo.isFollow === false" class="ms-3">
                            <el-button type="primary" @click="onFollow()">
                                팔로우
                            </el-button>
                        </div>
                        <div v-else-if="memberInfo.isFollow === true"  class="ms-3">
                            <el-button type="danger" @click="onFollow()">
                                팔로잉
                            </el-button>
                        </div>
                        <div v-else class="ms-3">
                        </div>
                    </div>
                </div>
            </div>
            <el-divider style="margin: 0"/>
            <el-menu class="activities" router :default-active="route.name" >
                <el-menu-item index="members-articles" @click="router.replace('/members/'+route.params.id+'/articles')">
                    작성글
                </el-menu-item>
                <el-menu-item  index="members-comments" @click="router.replace('/members/'+route.params.id+'/comments')">
                    작성댓글
                </el-menu-item>
                <el-menu-item index="members-liked-articles" @click="router.replace('/members/'+route.params.id+'/liked_articles')">
                    좋아요
                </el-menu-item>
                <el-menu-item  index="members-chatting-rooms" @click="router.replace('/members/'+route.params.id+'/chatting_rooms')">
                    1:1 대화방
                </el-menu-item>
            </el-menu>
        </div>
        <keep-alive>
            <Activities :key="activitiesKey" />
        </keep-alive>
    </div>
</template>
<script lang="ts" setup>
import {onMounted, provide, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useResponseStore} from "@/stores/Response";
import {follow, getActivities} from "@/controller/api/member";
import type {Activity} from "@/custom-types/activity";
import {Avatar} from "@element-plus/icons-vue";
import {useActivitiesStore} from "@/stores/activities";
import Activities from "@/components/member/Activities.vue";
import Pagination from "@/components/pagination/Pagination.vue";
import {usePageStore} from "@/stores/page";

const router = useRouter();
const route = useRoute();
const resStore = useResponseStore();
const activitiesStore = useActivitiesStore();
const pageStore = usePageStore();
const activitiesKey = ref(0);
const memberInfo = ref({
    id: 0,
    nickname: '',
    imageUrl:'',
    isFollow: Boolean,
})

const isSetPage = ref(false);

onMounted(async ()=> {
    await setDefaultFromPath(route.fullPath);
    await onGetActivities();
})

const onPaging = async function(page: number){
    await pageStore.setArticlesCurrentPage(page);
    isSetPage.value = true;
    await onGetActivities();
}

function setDefaultFromPath(path: string):void {
    const regex = /[?&]([^=]+)=([^&]+)/;

    const match = regex.exec(path);
    if (match) {
        const paramName = match[1];
        const paramValue = match[2];
        if(paramName === "page"){
            pageStore.setArticlesCurrentPage(parseInt(paramValue, 10));
            isSetPage.value = true;
        } else {
            pageStore.setArticlesCurrentPage(1);
        }
    }
}

const getPath = function(){
    let path = route.path;

    if(isSetPage.value) {
        path = path + '?' + 'page='+pageStore.getArticlesCurrentPage;
    }

    return path;
}
const onGetActivities = async function () {
    await getActivities(getPath())
        .then((response: any) => {
            if (resStore.isOK) {
                activitiesStore.clean();
                memberInfo.value.id = response.id;
                memberInfo.value.nickname = response.nickname;
                memberInfo.value.imageUrl = response.imagePath;
                memberInfo.value.isFollow = response.isFollow;

                response.activityList.forEach((r: any) => {
                    const activity: Activity = {...r}
                    activitiesStore.addActivities(activity);
                });
                pageStore.setArticlesMaxPage(response.maxPage);
                router.replace(getPath());
                activitiesKey.value++;
                scrollTo(0, 0);
            } else {
                alert(resStore.getErrorMessage);
                console.log(response)
                router.back();
            }
        }).catch(error => {
            alert(error);
            console.log(error);
            router.back();
        })
}

provide('onPaging', onPaging);

const onFollow = async function () {
    await follow(getPath()+"/follow")
        .then((response: any) => {
            if (resStore.isOK) {
                onGetActivities();
            } else {
                alert(resStore.getErrorMessage);
                console.log(response)
                onGetActivities();
            }
        }).catch(error => {
            alert(error);
            console.log(error);
            onGetActivities();
        })
}
</script>

<style scoped lang="scss">

.simple-profile ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 0.4rem;
  }
}

.profile-button {
  .el-button {
  }

  button {
    border-radius: 3px;
    border: none;
    background: none;
    cursor: pointer;

  }
}

.activities{
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    gap: 0;
    padding: 1px;
    border-bottom-left-radius: 10px;
    border-bottom-right-radius: 10px;

    .el-menu-item {
        flex-grow:1;
        justify-content:center;
        font-size: 1rem;
    }

    .el-menu-item:hover {
        color: #409eff;
        background-color: #ffffff;
    }

    .el-menu-item:active {
        color: #409eff;
    }

    .el-menu-item:first-child{
        border-bottom-left-radius: 10px;
    }

    .el-menu-item:last-child{
        border-bottom-right-radius: 10px;
    }
}
</style>