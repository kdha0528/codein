<template>
  <Header/>
  <div class="content">
    <el-form
        status-icon
        :rules="rules"
        label-width="120px"
        class="demo-ruleForm">
      <el-form-item label="Email" prop="email">
        <el-input v-model="profile.email"/>
      </el-form-item>
      <el-form-item label="Password" prop="password">
        <el-input v-model="profile.password" show-password/>
      </el-form-item>
      <el-form-item label="Confirm" prop="checkPassword">
        <el-input v-model="checkPassword" show-password autocomplete="off"/>
      </el-form-item>
      <el-form-item label="Name" prop="name">
        <el-input v-model="profile.name"/>
      </el-form-item>
      <el-form-item label="Nickname" prop="nickname">
        <el-input v-model="profile.nickname"/>
      </el-form-item>
      <el-form-item label="Phone" prop="phone">
        <el-input v-model="profile.phone"/>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="edit()">수정완료</el-button>
        <el-button type="danger" @click="cancel()">취소</el-button>
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

const profile = ref({
  email: null as null | '',
  name: null as null | '',
  password: null as null | '',
  nickname: null as null | '',
  phone: null as null | '',
});
const checkPassword = ref(null as null | '');

axios.get("/my-backend-api/members/{id}").then((response) => {
  profile.value.email = response.data.email;
  profile.value.name = response.data.name;
  profile.value.nickname = response.data.nickname;
  profile.value.phone = response.data.phone;
});

const validatePassword2 = (rule: any, value: any, callback: any) => {
  if (checkPassword.value !== profile.value.password) {
    callback(new Error("Two inputs don't match!"));
  } else {
    callback();
  }
};

const rules = reactive<FormRules>({
  checkPassword: [{validator: validatePassword2, trigger: 'blur'}],
});

const edit = function () {

  if (profile.value.email === '') profile.value.email = null;
  if (profile.value.password === '') profile.value.password = null;
  if (checkPassword.value === '') checkPassword.value = null;
  if (profile.value.name === '') profile.value.name = null;
  if (profile.value.nickname === '') profile.value.nickname = null;
  if (profile.value.phone === '') profile.value.phone = null;

  if (profile.value.password !== checkPassword.value) {
    alert("비밀번호가 일치하지 않습니다.");
    return;
  }

  axios.post('/my-backend-api/settings/profile', {
    email: profile.value.email,
    password: profile.value.password,
    name: profile.value.name,
    nickname: profile.value.nickname,
    phone: profile.value.phone,
  }).then((res: any) => {
    auth.logout();
    route.replace("home");
  }).catch(error => {
    if (error.response.data.code == "C001") {
      alert("양식에 맞지 않습니다.");
    } else if (error.response.data.code == "M001") {
      alert("이미 존재하는 이메일입니다.");
    } else if (error.response.data.code == "M007") {
      alert("이미 존재하는 전화번호입니다.");
    } else if (error.response.data.code == "M008") {
      alert("이미 존재하는 닉네임입니다.");
    } else {
      alert(error);
    }
    route.replace("/profilesettings");
  })


};
const cancel = function () {
  route.replace("/profile");
}
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
