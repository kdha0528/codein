<template>
  <Header/>
  <div class="content">
    <el-form
        status-icon
        :rules="rules"
        label-width="120px"
        class="demo-ruleForm">
      <el-form-item label="Email" prop="email">
        <el-input v-model="email"/>
      </el-form-item>
      <el-form-item label="Password" prop="password">
        <el-input v-model="password" show-password/>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="login()">Log in</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script lang="ts" setup>
import Header from '@/components/Header.vue';
import { reactive, ref } from 'vue';
import type { FormRules } from 'element-plus';
import axios from 'axios';
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";
import type { Profile } from "@/components/props/profile";

const auth = useAuthStore();
const route = useRouter();

const email = ref('');
const password = ref('');


const validateEmail = (rule: any, value: any, callback: any) => {
  if (email.value === '') {
    callback(new Error('Please input the email'));
  } else {
    callback();
  }
};

const validatePassword = (rule: any, value: any, callback: any) => {
  if (password.value === '') {
    callback(new Error('Please input the password'));
  } else {
    callback();
  }
};

const ruleForm = reactive({
  email: '',
  password: '',
});

const rules = reactive<FormRules>({
  email: [{validator: validateEmail, trigger: 'blur'}],
  password: [{validator: validatePassword, trigger: 'blur'}],
});


const login = function () {
  axios.post('/my-backend-api/login', {
    email: email.value,
    password: password.value,
  }).then((res: any) => {
    const member: Profile = {
      id: res.data.id,
      email: res.data.email,
      nickname: res.data.nickname,
      point: res.data.point,
      role: res.data.role,
    }

    auth.login(member);

    route.push('/home')
  }).catch(error => {
    if (error !== null) {
      
      if (error.response.data.code === "M002") {
        alert("이메일/비밀번호가 올바르지 않습니다.");
      } else if (error.response.code === "C001") {
        alert("로그인 양식에 맞지 않습니다.");
      } else {
        alert(error);
      }
    }
    route.push('/home')
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
