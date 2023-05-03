<template>
  <Header/>
  <div class="content">
    <div class="d-flex flex-column" style="border: thin solid #dcdfe6; border-radius: 10px;">
      <div class="d-flex flex-row align-items-center">
        <div class="m-3">
            <img v-if="member.imageUrl" :src="member.imageUrl"
                 style="max-width:5rem; max-height:5rem; width: 5rem; height: 5rem; border-radius: 100%; border: solid 1px black;"
                 alt=""/>
            <el-icon v-else size="40" class="ms-2"
                     style="width: 5rem;  height:5rem; border-radius: 100%; border:solid 1px black;">
                <UserFilled/>
            </el-icon>
        </div>
        <div class="simple-profile">
          <ul>
            <li>{{ member.nickname }}</li>
            <li>{{ member.role }}</li>
            <li>팔로우 팔로워</li>
          </ul>
        </div>
      </div>
      <el-menu class="activities" router :default-active="'/members/'+member.id" >
          <el-menu-item :index="'/members/'+member.id+'/articles'">
              작성글
          </el-menu-item>
          <el-menu-item :index="'/members/'+member.id+'/comments'">
              작성댓글
          </el-menu-item>
          <el-menu-item :index="'/members/'+member.id+'/liked-articles'">
              좋아요한 글
          </el-menu-item>
          <el-menu-item :index="'/members/'+member.id+'/chatting-rooms'">
              1:1 대화방
          </el-menu-item>
      </el-menu>
    </div>
    <RouterView/>
  </div>
</template>
<script lang="ts" setup>
import Header from '@/components/Header.vue';
import {onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useResponseStore} from "@/stores/Response";
import {getMember} from "@/api/member";

const member = ref({
    id: null as null | any,
    nickname: '',
    role:'',
    imageUrl:'',
})
const router = useRouter();
const route = useRoute();
const resStore = useResponseStore();


onMounted(()=> {
    onGetMembers(route.params.id)
  }
)
const onGetMembers = async function (id: any){
    await getMember(id)
        .then((response: any)=>{
            if(resStore.isOK){
                member.value.id = response.id;
                member.value.nickname = response.nickname;
                member.value.role = response.role;
                member.value.imageUrl = response.imagePath;
            } else {
                alert(resStore.getErrorMessage);
                console.log(response)
                router.go(-1);
            }
        }).catch(error => {
            alert(error);
            console.log(error);
            router.go(-1);
        })
}


</script>

<style scoped lang="scss">
@import "../components/css/contentBase.css";

.simple-profile ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 0.4rem;

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
  }
}

.simple-profile {
  ul {
    li {
      font-size: 0.9rem;
    }
  }
}

.profile-button {
  .el-button {
    border: none;
  }

  button {
    border-radius: 3px;
    border: none;
    background: none;
    cursor: pointer;

  }
}

.activities{
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  gap: 0;
  padding: 1px;
  border-bottom-left-radius: 10px;
  border-bottom-right-radius: 10px;
  border:none;

  .el-menu-item {
    flex-grow:1;
    justify-content:center;
  }

  .el-menu-item:first-child{
    border-bottom-left-radius: 10px;
  }

  .el-menu-item:last-child{
    border-bottom-right-radius: 10px;
  }
}
</style>