<script lang="ts" setup>
import {reactive, ref} from 'vue';
import type {FormInstance, FormRules} from 'element-plus';
import {useRouter} from 'vue-router';
import axios from 'axios';

const router = useRouter();

const ruleFormRef = ref<FormInstance>();

const email = ref('');
const password = ref('');
const checkPassword = ref('');
const name = ref('');
const birth = ref('2000-01-01');
const sex = ref('male');
const phone = ref('');

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

const validatePassword2 = (rule: any, value: any, callback: any) => {
  if (checkPassword.value === '') {
    callback(new Error('Please input the password again'));
  } else if (checkPassword.value !== password.value) {
    callback(new Error("Two inputs don't match!"));
  } else {
    callback();
  }
};

const checkName = (rule: any, value: any, callback: any) => {
  if (name.value === '') {
    callback(new Error('Please input your name'));
  } else {
    callback();
  }
};

const checkAge = (rule: any, value: any, callback: any) => {
  let today = new Date();
  if (today.getFullYear() - Number(birth.value.slice(0, 4)) < 13) {
    return callback(new Error('Age must be greater than 13'));
  } else if (today.getFullYear() - Number(birth.value.slice(0, 4)) == 13) {
    if (
        today.getMonth() - Number(birth.value.slice(5, 7)) < 0 &&
        today.getDate() - Number(birth.value.slice(8, 10)) < 0
    ) {
      return callback(new Error('Age must be greater than 13'));
    }
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

const ruleForm = reactive({
  email: '',
  password: '',
  checkPassword: '',
  name: '',
  birth: '',
  phone: '',
});

const rules = reactive<FormRules>({
  email: [{validator: validateEmail, trigger: 'blur'}],
  password: [{validator: validatePassword, trigger: 'blur'}],
  checkPassword: [{validator: validatePassword2, trigger: 'blur'}],
  name: [{validator: checkName, trigger: 'blur'}],
  birth: [{validator: checkAge, trigger: 'blur'}],
  phone: [{validator: checkPhone, trigger: 'blur'}],
});

const signup = function () {
  axios.post('/my-backend-api/signup', {
    email: email.value,
    password: password.value,
    name: name.value,
    birth: birth.value,
    sex: sex.value,
    phone: phone.value,
  }).then(() => {
    router.replace({name: "home"});
  })
};

const resetForm = function () {
  email.value = '';
  password.value = '';
  checkPassword.value = '';
  name.value = '';
  birth.value = '2000-01-01';
  phone.value = '';
  sex.value = 'male';
}

</script>

<template>
  <el-form
      status-icon
      :rules="rules"
      label-width="120px"
      class="demo-ruleForm">
    <el-form-item label="Email" prop="email">
      <el-input v-model="email" autocomplete="off"/>
    </el-form-item>
    <el-form-item label="Password" prop="password">
      <el-input v-model="password" autocomplete="off"/>
    </el-form-item>
    <el-form-item label="Confirm" prop="checkPassword">
      <el-input v-model="checkPassword" autocomplete="off"/>
    </el-form-item>
    <el-form-item label="Name" prop="name">
      <el-input v-model="name"/>
    </el-form-item>
    <div class="d-flex justify-content-start">
      <el-form-item label="Birth" prop="birth">
        <div class="demo-date-picker">
          <el-date-picker
              v-model="birth"
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
        <el-radio v-model="sex" label="male">Male</el-radio>
        <el-radio v-model="sex" label="female">Female</el-radio>
      </el-form-item>
    </div>
    <el-form-item label="Phone" prop="phone">
      <el-input v-model="phone"/>
    </el-form-item>

    <el-form-item>
      <el-button type="primary" @click="signup()">Sign up</el-button>
      <el-button @click="resetForm()">Reset</el-button>
    </el-form-item>
  </el-form>
</template>

<style scoped>
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
