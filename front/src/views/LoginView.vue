<template>
  <Header/>
  <div class="content">
    <el-form
        status-icon
        :rules="rules"
        label-width="120px"
        class="demo-ruleForm">
      <el-form-item label="Email" prop="email">
        <el-input v-model="loginForm.email"/>
      </el-form-item>
      <el-form-item label="Password" prop="password">
        <el-input v-model="loginForm.password" show-password/>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onLogin()">Log in</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script lang="ts" setup>
import Header from '@/components/Header.vue';
import { reactive, ref } from 'vue';
import type { FormRules } from 'element-plus';
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";
import type { Profile } from "@/components/props/profile";
import {login} from "@/api/member";
import {useResponseStore} from "@/stores/Response";

const auth = useAuthStore();
const router = useRouter();

const loginForm = ref({
    email: '',
    password: '',
})


const validateEmail = (rule: any, value: any, callback: any) => {
  if (loginForm.value.email === '') {
    callback(new Error('Please input the email'));
  } else {
    callback();
  }
};

const validatePassword = (rule: any, value: any, callback: any) => {
  if (loginForm.value.password === '') {
    callback(new Error('Please input the password'));
  } else {
    callback();
  }
};

const rules = reactive<FormRules>({
  email: [{validator: validateEmail, trigger: 'blur'}],
  password: [{validator: validatePassword, trigger: 'blur'}],
});

const onLogin = async function () {
    await login(loginForm.value)
        .then(async (res:any)=>{
            const member: Profile = {
              id: res.data.id,
              email: res.data.email,
              nickname: res.data.nickname,
              point: res.data.point,
              role: res.data.role,
            }
            auth.login(member);
            await router.push("home");
        }).catch((error:any)=>{
            const resStore = useResponseStore();
            if(resStore.isError) {
                switch (resStore.getErrorCode) {
                    case "C001":
                        alert("양식에 맞지 않습니다.");
                        break;
                    case "MOO2":
                        alert("이메일/비밀번호가 올바르지 않습니다.");
                        break;
                    default:
                        alert("Error : "+ error);
                        break;
                }
                console.log("error code : ",resStore.getErrorCode)
                router.replace("/login");
            }else{
                alert("Error : "+ error);
                router.replace("/login");
            }
        })
};

</script>

<style scoped>
@import "../components/css/contentBase.css";

.el-form-item {
  margin-bottom: 20px;
}

.cell {
  height: 30px;
  padding: 3px 0;
  box-sizing: border-box;
}

.cell .text {
  width: 24px;
  height: 24px;
  display: block;
  margin: 0 auto;
  line-height: 24px;
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  border-radius: 50%;
}

.cell.current .text {
  background: #626aef;
  color: #fff;
}
</style>
