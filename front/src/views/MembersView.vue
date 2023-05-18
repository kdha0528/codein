<template>
  <Header/>
  <RouterView :key="route.fullPath" />
</template>
<script lang="ts" setup>
import Header from '@/components/header/Header.vue';
import {useRoute, useRouter} from "vue-router";
const router = useRouter();
const route = useRoute();

</script>

<style scoped lang="scss">
@import "../components/css/contentBase.css";
</style>