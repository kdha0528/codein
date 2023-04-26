<template>
    <h2>비밀번호</h2>
    <el-button type="danger" href="/password">
        <el-icon style="vertical-align: middle">
            <Lock />
        </el-icon>
        <span style="vertical-align: middle"> 비밀번호 변경 </span>
    </el-button>


    <el-divider/>

    <h2>이메일</h2>
    <el-form
        status-icon
        method="post"
        class="demo-ruleForm">
      <el-form-item prop="email">
        <el-input v-model="emailForm"/>
      </el-form-item>
      <el-form-item>
          <el-button type="info" @click="onChangeEmail()">
              <el-icon style="vertical-align: middle">
                  <Message />
              </el-icon>
              <span style="vertical-align: middle"> 이메일 변경 </span>
          </el-button>
      </el-form-item>
    </el-form>

    <el-divider/>

    <h2>전화번호</h2>
    <el-form
        status-icon
        method="post"
        class="demo-ruleForm">
      <el-form-item prop="phone">
        <el-input v-model="phoneForm"/>
      </el-form-item>
      <el-form-item>
          <el-button type="info" @click="onChangePhone()">
              <el-icon style="vertical-align: middle">
                  <Iphone />
              </el-icon>
              <span style="vertical-align: middle"> 전화번호 변경 </span>
          </el-button>
      </el-form-item>
    </el-form>

    <el-divider/>

    <h2>계정 삭제</h2>
    <el-checkbox v-model="deleteCheckBox">계정 삭제에 동의합니다.</el-checkbox>
    <el-button type="danger" @click="onDelete()"  v-bind:disabled="!deleteCheckBox">
        <el-icon style="vertical-align: middle">
            <Delete />
        </el-icon>
        <span style="vertical-align: middle"> 회원 탈퇴 </span>
    </el-button>

</template>
<script lang="ts" setup>
import {onMounted, ref} from 'vue';
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";
import {Delete, Iphone, Message, Lock, Search} from '@element-plus/icons-vue'
import {changeEmail, changePassword, changePhone, deleteAccount, getAccount, getProfile} from "@/api/member";
import {useResponseStore} from "@/stores/Response";
const auth = useAuthStore();
const router = useRouter();
const resStore = useResponseStore();

const deleteCheckBox = ref(false);

const emailForm = ref('');
const phoneForm = ref('');


onMounted(()=>{onGetAccount()})
const onGetAccount = async function (){
    await getAccount()
        .then((response:any)=>{
            emailForm.value = response.data.email;
            phoneForm.value = response.data.phone;
        })
        .catch((error:any) => {
            if(resStore.isError) {
                alert("Error : " + error);
                auth.logout()
                console.log("error code : ",resStore.getErrorCode)
                router.push({name:"home"});
            }else{
                alert("Error : " + error);
                router.push({name:"home"});
            }
        });
}

const onChangeEmail = async function () {
    await changeEmail(emailForm.value)
        .then(()=>{
            alert("변경이 완료되었습니다.")
            auth.logout()
            router.push("home");
        }).catch((error:any)=>{
            if(resStore.isError) {
                switch (resStore.getErrorCode) {
                    case "C001":
                        alert("양식에 맞지 않습니다.");
                        break;
                    case "MOO1":
                        alert("이미 존재하는 이메일입니다.");
                        break;
                    default:
                        alert("Error : "+ error);
                        break;
                }
                console.log("error code : ",resStore.getErrorCode)
                router.replace("account");
            }else{
                alert("Error : "+ error);
                router.replace("account");
            }
        })
};

const onChangePhone = async function () {
    await changePhone(phoneForm.value)
        .then(()=>{
            alert("변경이 완료되었습니다.")
            auth.logout()
            router.push("home");
        }).catch((error:any)=>{
            if(resStore.isError) {
                switch (resStore.getErrorCode) {
                    case "C001":
                        alert("양식에 맞지 않습니다.");
                        break;
                    case "MOO7":
                        alert("이미 존재하는 전화번호입니다.");
                        break;
                    default:
                        alert("Error : "+ error);
                        break;
                }
                console.log("error code : ",resStore.getErrorCode)
                router.replace("account");
            }else{
                alert("Error : "+ error);
                router.replace("account");
            }
        })
};

const onDelete = async function () {
    await deleteAccount()
        .then(()=>{
            alert("삭제가 완료되었습니다.")
            auth.logout()
            router.push("home");
        }).catch((error:any)=>{
            if(resStore.isError) {
                console.log("error code : ",resStore.getErrorCode)
                alert("Error : "+ error);
                router.replace("account");
            }else{
                alert("Error : "+ error);
                router.replace("account");
            }
        })
};
</script>

<style scoped>
@import "../components/css/contentBase.css";

</style>
