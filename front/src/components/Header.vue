<template>
  <el-header>
    <el-menu mode="horizontal" router>
      <el-menu-item index="/home">Home</el-menu-item>
      <el-menu-item index="/signup">회원가입</el-menu-item>
    </el-menu>
  </el-header>
</template>

<script setup lang="ts"></script>

<style scoped lang="scss">
.header {
  padding: 0;
  width: 100vw;
}
</style>