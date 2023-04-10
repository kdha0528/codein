<template>
  <h2>회원정보</h2>

  <el-form
      status-icon
      :rules="rules"
      label-width="120px"
      label-position="top"
      class="demo-ruleForm">
    <el-form-item label="이름" prop="name">
      <el-input v-model="profile.name"/>
    </el-form-item>
    <el-form-item label="닉네임" prop="nickname">
      <el-input v-model="profile.nickname"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="edit()">저장</el-button>
    </el-form-item>
  </el-form>
</template>
<script lang="ts" setup>
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

  axios.post('/my-backend-api/settings/account/edit', {
    email: profile.value.email,
    password: profile.value.password,
    name: profile.value.name,
    nickname: profile.value.nickname,
    phone: profile.value.phone,
  }).then(() => {
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
    route.replace("/settings/profile");
  })


};
const cancel = function () {
  route.replace("/settings/profile");
}
</script>

<style scoped>
@import "../components/css/contentBase.css";

.el-form {
  margin-top: 2.5rem;
}
</style>