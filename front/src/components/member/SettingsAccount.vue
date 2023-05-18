<template>
    <h2>비밀번호</h2>
    <el-link href="/settings/account/password">
      <el-button type="danger">
          <el-icon style="vertical-align: middle">
              <Lock />
          </el-icon>
          <span style="vertical-align: middle"> 비밀번호 변경 </span>
      </el-button>
    </el-link>


    <el-divider/>

    <h2>이메일</h2>
    <el-form
        status-icon
        method="post"
        class="demo-ruleForm">
      <el-form-item prop="email">
        <el-input v-model="emailForm.email"/>
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
        <el-input v-model="phoneForm.phone"/>
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
import {changeEmail, changePassword, changePhone, deleteAccount, getAccount, getProfile} from "@/controller/api/member";
import {useResponseStore} from "@/stores/Response";
import Header from "@/components/header/Header.vue";
const auth = useAuthStore();
const router = useRouter();
const resStore = useResponseStore();

const deleteCheckBox = ref(false);

const emailForm = ref({
    email: ''
});
const phoneForm = ref({
    phone: '',
});


onMounted(()=>{onGetAccount()})
const onGetAccount = async function (){
    await getAccount()
        .then((response: any)=>{
            if(resStore.isOK){
                emailForm.value.email = response.email;
                phoneForm.value.phone = response.phone;
            } else {
                auth.logout()
                alert(resStore.getErrorMessage);
                console.log(response)
                router.push({name:"login"});
            }
        }).catch(error => {
            auth.logout()
            alert(error);
            console.log(error);
            router.push({name:"login"});
        })
}

const onChangeEmail = async function () {
    await changeEmail(emailForm.value)
        .then((response: any)=>{
            if(resStore.isOK){
                alert("변경이 완료되었습니다.")
                auth.logout()
                router.push({name:"login"});
            } else {
                alert(resStore.getErrorMessage);
                console.log(response)
                router.replace("account");
            }
        }).catch(error =>{
            if(resStore.isOK) {
                alert(error);
                console.log(error)
                auth.logout()
                router.push({name:"login"});
            }else{
                alert(error);
                console.log(error)
                router.replace("account");
            }
        })
};

const onChangePhone = async function () {
    await changePhone(phoneForm.value)
        .then((response: any)=>{
            if(resStore.isOK){
                alert("변경이 완료되었습니다.")
                auth.logout()
                router.push({name:"login"});
            } else {
                alert(resStore.getErrorMessage);
                console.log(response)
                router.replace("account");
            }
        }).catch(error =>{
            if(resStore.isOK) {
                alert(error);
                console.log(error)
                auth.logout()
                router.push({name:"login"});
            }else{
                alert(error);
                console.log(error)
                router.replace("account");
            }
        })
};

const onDelete = async function () {
    await deleteAccount()
        .then((response: any)=>{
            if(resStore.isOK){
                alert("삭제가 완료되었습니다.")
                auth.logout()
                router.push({name:"home"});
            } else {
                alert(resStore.getErrorMessage);
                console.log(response)
                auth.logout()
                router.push({name:"home"});
            }
        }).catch(error =>{
            alert(error);
            console.log(error)
            auth.logout()
            router.push({name:"home"});
        })
};
</script>

<style scoped>
@import "../css/contentBase.css";

</style>
