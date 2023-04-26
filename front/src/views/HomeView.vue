<template>
  <Header/>
  <div class="content">
    <ul>
      <li v-for="member in members" :key="member.id">
        <div class="email">
          {{ member.email }}
        </div>
        <div class="sub d-flex">
          <div class="id">{{ member.id }}</div>
          <div class="name">{{ member.name }}</div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">
import Header from '@/components/Header.vue';
import {onMounted, ref} from "vue";
import {getHome} from "@/api/member";
import {useResponseStore} from "@/stores/Response";
import {useRouter} from "vue-router";

const members = ref([]);
const router = useRouter();
const resStore = useResponseStore();

onMounted(()=>{onGetHome()})
const onGetHome = async function (){
    await getHome()
        .then((response:any)=>{
            response.data.forEach((r:any) => {
                members.value.push(r);
            });
        })
        .catch((error:any) => {
            if(resStore.isError) {
                console.log("error code : ",resStore.getErrorCode)
                alert("Error : "+error)
                router.replace("home");
            }else{
                alert("Error : "+error)
                router.replace("home");
            }
        });
}
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