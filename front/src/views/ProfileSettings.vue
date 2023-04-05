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
      <el-form-item label="Name" prop="name">
        <el-input v-model="name"/>
      </el-form-item>
      <el-form-item label="Phone" prop="phone">
        <el-input v-model="phone"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="edit()">수정완료</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script lang="ts" setup>
import Header from '@/components/Header.vue';
import { reactive, ref } from 'vue';
import type { FormRules } from 'element-plus';
import axios from 'axios';
import { authStorage } from "@/stores/auth";
import { useRouter } from "vue-router";

const auth = authStorage();
const route = useRouter();
const profile = auth.getProfile;

const profileSettings = ref({
  id: profile.id,
  email: profile.email,
  nickname: profile.nickname,
  point: profile.point,
  role: profile.role,
})


const validateEmail = (rule: any, value: any, callback: any) => {
  if (profile.email === '') {
    callback(new Error('Please input the email'));
  } else {
    callback();
  }
};

const checkNickname = (rule: any, value: any, callback: any) => {
  if (profile.nickname === '') {
    callback(new Error('Please input your nickname'));
  } else {
    callback();
  }
};

const checkPhone = (rule: any, value: any, callback: any) => {
  if (phone.value === '') {
    callback(new Error('Please input your phone number'));
  } else {
    callback();
  }
};


if (profile != null) {
  const resetForm = function () {
    email.value = profile.value.member.email
    password.value = '';
    name.value = '';
    phone.value = '';
  }
}

const ruleForm = reactive({
  email: '',
  password: '',
  name: '',
  phone: '',
});

const rules = reactive<FormRules>({
  email: [{validator: validateEmail, trigger: 'blur'}],
  password: [{validator: validatePassword, trigger: 'blur'}],
  name: [{validator: checkName, trigger: 'blur'}],
  phone: [{validator: checkPhone, trigger: 'blur'}],
});


const edit = function () {
  axios.post('/my-backend-api/editmember', {
    email: email.value,
    password: password.value,
    name: name.value,
    phone: phone.value,
  }).then((res: any) => {
    auth.logout();
    route.replace("home");
  }).catch(error => {
    if (error.response.data.code == "C001") {
      alert("양식에 맞지 않습니다.");
    } else if (error.response.data.code == "M001") {
      alert("이미 존재하는 이메일 입니다.");
    } else if (error.response.data.code == "M007") {
      alert("이미 존재하는 전화번호 입니다.");
    } else {
      alert(error);
    }
    email.value = '';
    password.value = '';
    name.value = '';
    phone.value = '';
    route.replace("/profilesettings");
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
