<template>
    <el-header height="80px">
        <el-menu mode="horizontal" router class="navbar d-flex justify-content-between">
            <div class="d-flex align-items-center justify-content-center" >
                <div class="d-flex align-items-center justify-content-center ms-3" @click="router.replace('/community')" style="cursor: pointer">
                    <el-icon size="40">
                        <Loading />
                    </el-icon>
                    <div class="d-flex ms-1 me-2" style="font-size: larger; font-weight: bold;">Code In</div>
                </div>
                <el-menu-item index="/community" style="font-size: 1rem; font-weight: 500;">
                    커뮤니티
                </el-menu-item>
                <el-menu-item index="/question" style="font-size: 1rem; font-weight: 500;">
                    Q&A
                </el-menu-item>
                <el-menu-item index="/information" style="font-size: 1rem; font-weight: 500;">
                    정보공유
                </el-menu-item>
                <el-menu-item index="/notice" style="font-size: 1rem; font-weight: 500;">
                    공지사항
                </el-menu-item>
            </div>
            <div v-if="isUserLogin" class="position-relative d-flex justify-content-center align-items-center me-4">
                <div class="position-relative me-3" style="cursor: pointer;" @click="clickNotification">
                    <keep-alive>
                        <div class="new_notification_count" v-if="auth.notificationCount > 0" :key="auth.notificationCount">
                            {{ auth.notificationCount }}
                        </div>
                    </keep-alive>
                    <div class="notification_button">
                        <el-icon type="info" size="30" style="width: 3rem; height:3rem; color:#A6A6A6;">
                            <Bell/>
                        </el-icon>
                    </div>
                    <div v-if="clickedNotification" style="overflow: auto; border: none;">
                        <Notifications/>
                    </div>
                </div>
                <div  class="d-flex justify-content-center align-items-center" style="cursor: pointer;" @click="clickedNotification = false">
                    <el-dropdown trigger="click">
                        <span class="el-dropdown-link" :key="auth.getProfileImage">
                            <img v-if="auth.getProfileImage" :src="auth.getProfileImage"
                                 style="width: 2.8rem; height: 2.8rem; border-radius: 100%;"
                                 alt=""/>
                            <el-icon v-else size="40"
                                     style="width: 2.8rem;  height:2.8rem; border-radius: 100%; color:white; background-color: #E2E2E2;">
                                <Avatar/>
                            </el-icon>
                        </span>
                        <template #dropdown>
                            <el-dropdown-menu class="dropdown_menu d-flex flex-column"
                                              style="
                                            --el-dropdown-menuItem-hover-fill: white;
                                            --el-menu-hover-bg-color: white;
                                            --el-menu-hover-text-color: #409eff;
                                            --el-menu-active-color: #409eff">
                                <el-dropdown-item class="d-flex" style="padding-right: 5rem;" >
                                    <el-menu-item class="dropdown" index="/settings/profile">
                                        <el-icon>
                                            <User />
                                        </el-icon>
                                        내 프로필
                                    </el-menu-item>
                                </el-dropdown-item>
                                <el-dropdown-item class="d-flex">
                                    <el-menu-item index="/settings/account">
                                        <el-icon>
                                            <Setting />
                                        </el-icon>
                                        내 계정
                                    </el-menu-item>
                                </el-dropdown-item>
                                <el-dropdown-item class="d-flex">
                                    <el-menu-item :index="toActivity()" >
                                        <el-icon>
                                            <Clock />
                                        </el-icon>
                                        내 활동
                                    </el-menu-item>
                                </el-dropdown-item>
                                <el-dropdown-item divided class="d-flex">
                                    <el-menu-item index="logout" @click="onLogout">
                                        <el-icon>
                                            <CloseBold/>
                                        </el-icon>
                                        로그아웃
                                    </el-menu-item>
                                </el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
            </div>
            <div v-else class="d-flex justify-content-center align-items-center me-4">
                <el-menu-item index="/signup">회원가입</el-menu-item>
                <el-menu-item index="/login">로그인</el-menu-item>
            </div>
        </el-menu>
    </el-header>
</template>
<script setup lang="ts">
import {onMounted, provide, ref} from "vue";
import type {Notification} from "@/custom-types/notification";
import { useAuthStore } from "@/stores/auth";
import { useRouter, useRoute } from "vue-router";
import { logout } from "@/controller/api/member";
import {useResponseStore} from "@/stores/Response";
import {Loading, Avatar, CloseBold, Setting} from "@element-plus/icons-vue";
import {loadNotifications} from "@/controller/api/notificaion";
import Notifications from "@/components/header/Notifications.vue";
import InfiniteLoading from "v3-infinite-loading";
import "v3-infinite-loading/lib/style.css";
import {useNotificationStore} from "@/stores/notifications";

const auth = useAuthStore();
const router = useRouter();
const route = useRoute();
const resStore = useResponseStore();
const notificationStore = useNotificationStore();

const isUserLogin = ref(auth.isLoggedIn);
const clickedNotification = ref(false);

const clickNotification = function () {
    clickedNotification.value = !clickedNotification.value;
    auth.checkedNotifications();
    notificationStore.clean();
}

const toActivity = function(): string {
    return '/members/' + auth.getProfile.id;
}

router.afterEach((to, from, next) => {
    isUserLogin.value = auth.isLoggedIn;
});

const onLogout = async function () {
    await logout()
        .then((response: any)=>{
            if(resStore.isOK){
                auth.logout();
                router.go(-1);
            } else {
                auth.logout();
                alert(resStore.getErrorMessage);
                console.log(response)
                router.push({name:"community"});
            }
        }).catch(error => {
            auth.logout();
            alert(error);
            console.log(error);
            router.push({name:"community"});
        })
}

const getNotificationPath = function() {
    let path = "/notifications";
    if(notificationStore.getSize > 0) {
        return path+"?lastId="+ notificationStore.getLastId;
    } else {
        return path;
    }
}

const onLoadNotifications = async function () {
    notificationStore.startLoading();
    if(notificationStore.isNoMore){
        notificationStore.finishLoading();
    } else {
        await loadNotifications(getNotificationPath())
            .then((response: any) => {
                if (resStore.isOK) {
                    if (response.notificationListItemList.length < 20) {
                        notificationStore.noMoreNotification();
                    }
                    response.notificationListItemList.forEach((r: any) => {
                        const notification: Notification = {...r}
                        notificationStore.addNotification(notification);
                    })
                    notificationStore.finishLoading();
                } else {
                    notificationStore.finishLoading();
                    alert(resStore.getErrorMessage);
                    console.log(response)
                }
            }).catch(error => {
                notificationStore.finishLoading();
                alert(error);
                console.log(error)
            })
    }
}

provide('onLoadNotifications', onLoadNotifications);

</script>
<style scoped lang="scss">
.navbar{
    width: 100vw;
    height: 80px;
    position: fixed;
    top:0;
    left:0;
    right:0;
    z-index: 99;

    .el-menu-item {
        --el-menu-hover-bg-color: #ffffff;
        --el-menu-hover-text-color: #409eff;
        --el-menu-active-color:  #303133;
    }

}
.dropdown_menu{
    .el-menu-item {
        font-size: 1rem;
        .el-icon{
            font-size:22px;
            width: 2.2rem;
            height: 2.2rem;
        }
    }
}

.new_notification_count{
    position: absolute;
    z-index: 99;
    width:1.4rem;
    height:1.4rem;
    border-radius: 50%;
    color: white;
    background-color: red;
    font-size: 0.8rem;
    text-align: center;
    line-height: 1.5rem;
    right:0;
}


</style>