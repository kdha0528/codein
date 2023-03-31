<script setup lang="ts">
import Content from '@/components/Content.vue';
import Header from '@/components/Header.vue';</script>

<template>
  <div class="d-flex flex-column" style="max-width:960px; margin: 0 auto;">
    <Header/>
    <Content/>
  </div>
</template>

<style>
body {
  font-family: Pretendard, -apple-system, BlinkMacSystemFont, system-ui, Roboto, 'Helvetica Neue',
  'Segoe UI', 'Apple SD Gothic Neo', 'Noto Sans KR', 'Malgun Gothic', 'Apple Color Emoji',
  'Segoe UI Emoji', 'Segoe UI Symbol', sans-serif;
}
</style>
