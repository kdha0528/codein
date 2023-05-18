<template>
  <Header/>
  <RouterView :key="route.fullPath"/>
</template>

<script setup lang="ts">
import Header from '@/components/header/Header.vue';
import {useRouter, useRoute} from "vue-router";
const router = useRouter();
const route= useRoute();
</script>

<style scoped lang="scss">
@import "../components/css/contentBase.css";

</style>