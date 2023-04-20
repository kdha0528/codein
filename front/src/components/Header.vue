<template>
  <div v-if="isUserLogin">
    <el-header>
      <el-menu mode="horizontal" router>
        <el-menu-item index="/home">Home</el-menu-item>
        <el-menu-item index="/settings/profile">개인정보</el-menu-item>
        <el-menu-item index="/logout" @click="logout">로그아웃</el-menu-item>
      </el-menu>
    </el-header>
  </div>
  <div v-else>
    <el-header>
      <el-menu mode="horizontal" router>
        <el-menu-item index="/home">Home</el-menu-item>
        <el-menu-item index="/signup">회원가입</el-menu-item>
        <el-menu-item index="/login">로그인</el-menu-item>
      </el-menu>
    </el-header>
  </div>
</template>
<script setup lang="ts">
import axios from "axios";
import { ref } from "vue";
import router from "@/router";
import { useAuthStore } from "@/stores/auth"
import { useRouter } from "vue-router";

const auth = useAuthStore();
const route = useRouter();

const isUserLogin = ref(auth.isLoggedIn);

router.afterEach((to, from, next) => {
  isUserLogin.value = auth.isLoggedIn;
});

const logout = function () {
  axios.post("/my-backend-api/logout", {}).then(() => {
    auth.logout();
    route.replace({name: "home"});
  }).catch(
      error => {
        console.log(error);
        alert("오류가 발생했습니다.");
        route.replace({name: "home"});
      }
  )
}
</script>
<style scoped lang="scss">
.header {
  padding: 0;
  width: 100vw;
}

</style>