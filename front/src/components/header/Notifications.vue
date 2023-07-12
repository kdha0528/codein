<template>
    <div class="notification_window">
        <div class="mt-2" style="width: 100%; text-align: center; color:black; cursor:default; pointer-events: none;">
            알림
        </div>
        <el-divider class="mt-2 mb-3"/>
        <div v-for="notification in notificationStore.getNotifications"
             :key="notification.id"
             class="notification_item">
            <div class="d-flex ms-3" style="width:100%;">
                <div>
                    <img v-if="notification.senderImageUrl" :src="notification.senderImageUrl"
                       @click="router.replace( '/members/'+notification.senderId)"
                       style="width: 3rem; height: 3rem; border-radius: 100%; cursor: pointer;"
                       alt=""/>
                    <el-icon v-else size="40"
                           @click="router.replace( '/members/'+notification.senderId)"
                           style="width: 3rem;  height:3rem; border-radius: 100%; color:white; background-color: #E2E2E2;  cursor: pointer;">
                        <Avatar/>
                    </el-icon>
                </div>
                <div class="d-flex flex-column ms-3" style="width:74%;">
                    <div class="d-flex justify-content-between">
                        <div class="d-flex">
                            <div style="font-size: 1rem; cursor: pointer;"
                               @click="router.replace( '/members/'+notification.senderId)">
                              {{ notification.senderNickname }}
                            </div>
                            <div class="ms-2" style="font-size: 0.9rem; color: #A6A6A6">
                                {{ notification.subContent }}
                            </div>
                        </div>
                        <div style="font-size: 0.9rem; color: #A6A6A6">
                            {{ notification.notifiedAt }}
                        </div>
                    </div>
                    <div style="font-size: 0.9rem; cursor: pointer;"
                         @click="router.replace( '/articles/'+notification.articleId)">
                        {{ notification.content }}
                    </div>
                </div>
            </div>
            <el-divider class="mt-3"/>
        </div>
        <InfiniteLoading
            class="loader text-center mt-3 mb-3"
            @infinite="onLoad"
            distance="50"
        >
            <template #complete>
                <span>No more notification</span>
            </template>
            <template #spinner>
                <span>loading...</span>
            </template>
        </InfiniteLoading>
    </div>
</template>

<script lang="ts" setup>
import InfiniteLoading from "v3-infinite-loading";
import {Avatar} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
import {inject, toRefs} from "vue";
import {useNotificationStore} from "@/stores/notifications";

const notificationStore = useNotificationStore();
const router = useRouter();

const onLoad = async ($state: any) => {
    if(notificationStore.isNoMore) {
        await $state.complete();
    } else {
        await onLoadNotifications();
    }
}

const onLoadNotifications: any = inject('onLoadNotifications');

const moveToContent = function (){

}

</script>
<style>
.notification_window{
    position: absolute;
    right: -3rem;
    top: 3.5rem;

    width: 25rem;
    min-height: 30rem;
    max-height: 30rem;

    border: solid thin #E2E2E2;
    border-radius: 2px;

    background-color: white;

    overflow: auto;
}
.notification_window::-webkit-scrollbar {
    display: none;
}
</style>