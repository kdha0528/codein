<template>
  <div v-if="isUserLogin">
    <el-header>
      <el-menu mode="horizontal" router>
        <el-menu-item index="/">Home</el-menu-item>
        <el-menu-item index="/settings/profile">개인정보</el-menu-item>
        <el-menu-item index="/logout" @click="onLogout">로그아웃</el-menu-item>
      </el-menu>
    </el-header>
  </div>
  <div v-else>
    <el-header>
      <el-menu mode="horizontal" router>
        <el-menu-item index="/">Home</el-menu-item>
        <el-menu-item index="/signup">회원가입</el-menu-item>
        <el-menu-item index="/login">로그인</el-menu-item>
      </el-menu>
    </el-header>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import { useAuthStore } from "@/stores/auth";
import { useRouter, useRoute } from "vue-router";
import { logout } from "@/api/member";
import {useResponseStore} from "@/stores/Response";

const auth = useAuthStore();
const router = useRouter();
const route = useRoute();
const resStore = useResponseStore();

const isUserLogin = ref(auth.isLoggedIn);

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
</script>
<style scoped lang="scss">
.header {
  padding: 0;
  width: 100vw;
}

</style>