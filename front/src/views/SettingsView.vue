<template>
  <Header/>
  <div class="content">
    <h2>내 계정</h2>
    <el-menu router default-active="/settings/profile">
      <el-menu-item index="/settings/profile">
        <User style="width: 1rem; height: 1rem; margin-right: 8px"/>
        회원정보
      </el-menu-item>
      <el-menu-item index="/settings/account">
        <Setting style="width: 1rem; height: 1rem; margin-right: 8px"/>
        계정
      </el-menu-item>
    </el-menu>
    <el-divider/>
    <RouterView/>
  </div>
</template>
<script lang="ts" setup>
import Header from '@/components/Header.vue';
import { reactive, toRefs } from "vue";
import { useRouter } from "vue-router";

const route = useRouter();

const state = reactive({
  circleUrl:
      'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
  squareUrl:
      'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png',
  sizeList: ['small', '', 'large'] as const,
})
const {circleUrl, squareUrl, sizeList} = toRefs(state)
</script>

<style scoped lang="scss">
@import "../components/css/contentBase.css";

.el-menu {
  border: none;

  .el-menu-item {
    border-radius: 8px;
    margin-bottom: 1rem;
    font-size: 1rem;
    height: 2.7rem;
  }
}

.el-divider {
  margin-top: 2rem;
  margin-bottom: 2rem;
}
</style>