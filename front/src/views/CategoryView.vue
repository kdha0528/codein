<template>
  <Header/>
  <RouterView/>
</template>

<script setup lang="ts">
import Header from '@/components/Header.vue';
import {useRouter} from "vue-router";
const router = useRouter();

</script>

<style scoped lang="scss">
@import "../components/css/contentBase.css";

ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 2rem;

    .title {
      a {
        font-size: 1.1rem;
        text-decoration: none;
        color: #383838;
      }

      &:hover {
        text-decoration: underline;
      }
    }

    .content {
      font-size: 0.85rem;
      margin-top: 8px;
      color: #7e7e7e;
    }

    &:last-child {
      margin-bottom: 0;
    }

    .sub {
      margin-top: 8px;
      font-size: 0.78rem;

      .name {
        margin-left: 10px;
        color: #6b6b6b;
      }
    }
  }
}
</style>