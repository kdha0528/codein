<template>
  <Header/>
      <el-table :data="members" height="70vh" border style="width: 90vw" class="mt-5 ms-5">
          <el-table-column prop= id label="ID" width="180">
          </el-table-column>
          <el-table-column prop= email label="Email" width="180">
          </el-table-column>
          <el-table-column prop= name label="Name" >
              <template v-slot:default="table">
                  <el-link :href="'/members/'+table.row.id">
                      {{ table.row.name }}
                  </el-link>
              </template>
          </el-table-column>
      </el-table>
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
        .then((response: any)=>{
            if(resStore.isOK){
                response.forEach((r:any) => {
                    members.value.push(r);
                });
                console.log(members);
            } else {
                alert(resStore.getErrorMessage);
                console.log(response)
                router.push({name:"home"});
            }
        }).catch(error => {
            alert(error);
            console.log(error);
            router.push({name:"home"});
        })
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