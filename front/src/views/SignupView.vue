<template>
  <Header/>
  <div class="content">
    <el-form
        status-icon
        :rules="rules"
        label-width="120px"
        class="demo-ruleForm">
      <el-form-item label="Email" prop="email">
        <el-input v-model="signupForm.email" autocomplete="off"/>
      </el-form-item>
      <el-form-item label="Password" prop="password">
        <el-input v-model="signupForm.password" show-password autocomplete="off"/>
      </el-form-item>
      <el-form-item label="Confirm" prop="checkPassword">
        <el-input v-model="signupForm.checkPassword" show-password autocomplete="off"/>
      </el-form-item>
      <el-form-item label="Name" prop="name">
        <el-input v-model="signupForm.name"/>
      </el-form-item>
      <el-form-item label="Nickname" prop="nickname">
        <el-input v-model="signupForm.nickname"/>
      </el-form-item>
      <div class="d-flex justify-content-start">
        <el-form-item label="Birth" prop="birth">
          <div class="demo-date-picker">
            <el-date-picker
                v-model="signupForm.birth"
                type="date"
                placeholder="Pick a day"
                format="YYYY/MM/DD"
                value-format="YYYY-MM-DD"
            >
              <template #default="cell">
                <div class="cell" :class="{ current: cell.isCurrent }">
                  <span class="text">{{ cell.text }}</span>
                </div>
              </template>
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item label="" prop="sex">
          <el-radio v-model="signupForm.sex" label="male">Male</el-radio>
          <el-radio v-model="signupForm.sex" label="female">Female</el-radio>
        </el-form-item>
      </div>
      <el-form-item label="Phone" prop="phone">
        <el-input v-model="signupForm.phone"/>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="onSignup()">Sign up</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script lang="ts" setup>
import Header from '@/components/Header.vue';
import { reactive, ref } from 'vue';
import type { FormRules } from 'element-plus';
import { useRouter } from 'vue-router';
import { signup } from '@/api/member';
import {useResponseStore} from '@/stores/Response';

const router = useRouter();

const signupForm = ref({
    email: '',
    password:'',
    checkPassword:'',
    name:'',
    nickname:'',
    birth:'2000-01-01',
    sex:'male',
    phone:'',
})

const validateEmail = (rule: any, value: any, callback: any) => {
  if (signupForm.value.email === '') {
    callback(new Error('Please input the email'));
  } else {
    callback();
  }
};

const validatePassword = (rule: any, value: any, callback: any) => {
  if (signupForm.value.password === '') {
    callback(new Error('Please input the password'));
  } else {
    callback();
  }
};

const validatePassword2 = (rule: any, value: any, callback: any) => {
  if (signupForm.value.checkPassword === '') {
    callback(new Error('Please input the password again'));
  } else if (signupForm.value.checkPassword !== signupForm.value.password) {
    callback(new Error("Two inputs don't match!"));
  } else {
    callback();
  }
};

const checkNickname = (rule: any, value: any, callback: any) => {
  if (signupForm.value.nickname === '') {
    callback(new Error('Please input your nick name'));
  } else {
    callback();
  }
};

const checkName = (rule: any, value: any, callback: any) => {
  if (signupForm.value.name === '') {
    callback(new Error('Please input your name'));
  } else {
    callback();
  }
};

const checkAge = (rule: any, value: any, callback: any) => {
  let today = new Date();
  if (today.getFullYear() - Number(signupForm.value.birth.slice(0, 4)) < 13) {
    return callback(new Error('Age must be greater than 13'));
  } else if (today.getFullYear() - Number(signupForm.value.birth.slice(0, 4)) == 13) {
    if (
        today.getMonth() - Number(signupForm.value.birth.slice(5, 7)) < 0 &&
        today.getDate() - Number(signupForm.value.birth.slice(8, 10)) < 0
    ) {
      return callback(new Error('Age must be greater than 13'));
    }
  } else {
    callback();
  }
};

const checkPhone = (rule: any, value: any, callback: any) => {
  if (signupForm.value.phone === '') {
    callback(new Error('Please input your phone number'));
  } else {
    callback();
  }
};

const rules = reactive<FormRules>({
  email: [{validator: validateEmail, trigger: 'blur'}],
  password: [{validator: validatePassword, trigger: 'blur'}],
  checkPassword: [{validator: validatePassword2, trigger: 'blur'}],
  name: [{validator: checkName, trigger: 'blur'}],
  nickname: [{validator: checkNickname, trigger: 'blur'}],
  birth: [{validator: checkAge, trigger: 'blur'}],
  phone: [{validator: checkPhone, trigger: 'blur'}],
});

const onSignup = async function () {
    const response = await signup(signupForm.value)
        .then(()=>{
            alert("회원가입이 성공적으로 완료되었습니다.")
            router.push("home");
        }).catch((error)=>{
            const resStore = useResponseStore();
            console.log("res type = " + !resStore.isError)
            console.log("error = " + error)
            if(resStore.isError) {
                switch (resStore.getErrorCode) {
                    case "C001":
                        alert("회원가입 양식에 맞지 않습니다.");
                        break;
                    case "MOO1":
                        alert("이미 존재하는 이메일입니다.");
                        break;
                    case "M007":
                        alert("이미 존재하는 전화번호입니다.");
                        break;
                    case "M008":
                        alert("이미 존재하는 닉네임입니다.");
                        break;
                    default:
                        alert("Error Code : "+ response);
                        break;
                }
                router.replace("signup");
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
