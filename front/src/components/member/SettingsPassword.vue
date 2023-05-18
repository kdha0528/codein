<template>
    <Header/>
    <div class="content">
        <el-form
                status-icon
                :rules="rules"
                label-width="120px"
                class="demo-ruleForm">
            <el-form-item label="현재 비밀번호" prop="originPassword">
                <el-input v-model="passwordForm.originPassword " show-password/>
            </el-form-item>
            <el-form-item label="신규 비밀번호" prop="newPassword">
                <el-input v-model="passwordForm.newPassword" show-password/>
            </el-form-item>
            <el-form-item label="신규 비밀번호 확인" prop="validPassword">
                <el-input v-model="passwordForm.checkPassword" show-password/>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="onChangePassword()">수정 완료</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>
<script lang="ts" setup>
import Header from '@/components/header/Header.vue';
import { reactive, ref } from 'vue';
import type { FormRules } from 'element-plus';
import { useAuthStore } from "@/stores/auth";
import {useRoute, useRouter} from "vue-router";
import {changePassword} from "@/controller/api/member";
import {useResponseStore} from "@/stores/Response";
const auth = useAuthStore();
const router = useRouter();
const route = useRoute();
const resStore = useResponseStore();

const passwordForm = ref({
    originPassword:'',
    newPassword:'',
    checkPassword:'',
})

const validateOriginPassword = (rule: any, value: any, callback: any) => {
    if (passwordForm.value.originPassword === '') {
        callback(new Error('Please input the original password'));
    } else {
        callback();
    }
};
const validateNewPassword = (rule: any, value: any, callback: any) => {
    if (passwordForm.value.newPassword === '') {
        callback(new Error('Please input the new password'));
    } else {
        callback();
    }
};

const validateCheckPassword = (rule: any, value: any, callback: any) => {
    if (passwordForm.value.checkPassword === '') {
        callback(new Error('Please input the checking password'));
    } else {
        callback();
    }
};

const rules = reactive<FormRules>({
    originPassword: [{validator: validateOriginPassword, trigger: 'blur'}],
    newPassword: [{validator: validateNewPassword, trigger: 'blur'}],
    checkPassword: [{validator: validateCheckPassword, trigger: 'blur'}],
});

const onChangePassword = async function () {
    await changePassword(passwordForm.value)
        .then((response: any)=>{
            if(resStore.isOK){
                alert("변경이 완료되었습니다.")
                auth.logout()
                router.push({name:"login"});
            } else {
                alert(resStore.getErrorMessage);
                console.log(response);
                auth.logout()
                router.push({name:"login"});
            }
        }).catch(error => {
            alert(error);
            console.log(error);
            auth.logout()
            router.push({name:"login"});
        })
};

</script>

<style scoped>
@import "../css/contentBase.css";

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
