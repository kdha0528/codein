<script setup lang="ts">
import {ref} from "vue";
import {useRouter} from "vue-router";
import axios from "axios";


const router = useRouter()

const members = ref([]);


axios.get("/my-backend-api/?page=1&size=5").then((response) => {
  response.data.forEach((r: any) => {
    members.value.push(r);
  });
});
</script>

<template>
  <ul>
    <li v-for="member in members" :key="member.id">
      <div class="email">
        {{ member.email }}
      </div>
      <div class="sub d-flex">
        <div class="id">{{member.id}}</div>
        <div class="name">{{member.name}}</div>
      </div>
    </li>
  </ul>
</template>

<style scoped lang="scss">
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