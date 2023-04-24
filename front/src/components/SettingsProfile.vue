<template>
  <h2>회원정보</h2>

  <el-form
      status-icon
      label-width="120px"
      label-position="top"
      method="post"
      enctype="multipart/form-data"
      class="demo-ruleForm">
    <el-form-item label="이름" prop="name">
      <el-input v-model='profile.name'/>
    </el-form-item>
    <el-form-item label="닉네임" prop="nickname">
      <el-input v-model='profile.nickname'/>
    </el-form-item>
    <el-form-item prop="image" class="mt-5">
      <el-upload
          action="#"
          method="get"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
          accept="image/*"
      >
        <img v-if="imageUrl" :src="imageUrl"
             class="ms-2"
             style="max-width:5rem; max-height:5rem; width: 5rem; height: 5rem; border-radius: 100%; border: solid 1px black;"
             alt=""/>
        <el-icon v-else size="40" class="ms-2"
                 style="width: 5rem;  height:5rem; border-radius: 100%; border:solid 1px black;">
          <UserFilled/>
        </el-icon>
        <el-button type="primary" class="ms-4">Select</el-button>
        <template #tip>
          <div class="el-upload__tip text-danger mt-3">
            jpg/png files with a size less than 2MB
          </div>
        </template>
      </el-upload>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="edit()"
                 style="margin:2rem 0 auto auto; width:5rem; height:2.5rem; font-size: 1.1rem;">저장
      </el-button>
    </el-form-item>
  </el-form>
</template>
<script lang="ts" setup>
import {onMounted, ref} from 'vue';
import axios from 'axios';
import { useAuthStore } from "@/stores/auth";
import { useRouter } from "vue-router";
import type { UploadProps } from 'element-plus'
import { ElMessage } from "element-plus";
import {getSettingsProfile} from "@/api/member";
import {useResponseStore} from "@/stores/Response";

const auth = useAuthStore();
const router = useRouter();

const imageUrl = ref('')
const profile = ref({
  name: '',
  nickname: '',
})

const formData = new FormData();

let imageChanged = false;


const handleAvatarSuccess: UploadProps['onSuccess'] = (
    response,
    uploadFile
) => {
  imageChanged = true
  imageUrl.value = URL.createObjectURL(uploadFile.raw!)
  formData.set("profileImage", uploadFile.raw!)
}

const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  if (rawFile.size / 1024 / 1024 > 2) {
    ElMessage.error('Profile picture size can not exceed 2MB!')
    return false
  }
  return true
}

onMounted(()=>{onGetSettingsProfile()})
const onGetSettingsProfile = async function (){
    const response = await getSettingsProfile()
        .then((response:any)=>{
            imageUrl.value = response.data.imagePath;
            profile.value.name = response.data.name;
            profile.value.nickname = response.data.nickname;
        })
        .catch((error) => {
            const resStore = useResponseStore();
            console.log("res type = " + !resStore.isError)
            console.log("error = " + error)
            if(resStore.isError) {
                alert(error);
                auth.logout()
                router.replace("home");
            }
        });
}

const edit = function () {
  formData.append("name", profile.value.name)
  formData.append("nickname", profile.value.nickname)
  if (imageChanged) {
    axios.post('/my-backend-api/settings/profile', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      }
    }).then(() => {
      router.back();
    }).catch(error => {
      if (error.response.data.code == "C001") {
        alert("양식에 맞지 않습니다.");
      } else if (error.response.data.code == "M008") {
        alert("이미 존재하는 닉네임입니다.");
      } else if (error.response.data.code == "I001") {
        alert("사진 용량 초과입니다.");
      } else {
        alert(error);
      }
      router.replace("/settings/profile");
    })
  } else {
    axios.post('/my-backend-api/settings/profile', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      }
    }).then(() => {
      router.back();
    }).catch(error => {
      if (error.response.data.code == "C001") {
        alert("양식에 맞지 않습니다.");
      } else if (error.response.data.code == "M008") {
        alert("이미 존재하는 닉네임입니다.");
      } else {
        alert(error);
      }
      router.replace("/settings/profile");
    })
  }
};
const cancel = function () {
  router.replace("/settings/profile");
}
</script>

<style scoped>
@import "../components/css/contentBase.css";

.el-form {
  margin-top: 2.5rem;
}
</style>