<template>
  <Header/>
  <div class="content">
    <div class="d-flex flex-column">
      <div class="d-flex flex-row align-items-center mb-2">
        <div class="me-3">
          <el-row class="demo-avatar demo-basic">
            <el-col>
              <div class="demo-basic--circle">
                <div class="block">
                  <el-avatar :size="80" :src="circleUrl"/>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
        <div class="simpleProfile">
          <ul>
            <li>{{ profile.nickname }}</li>
            <li>{{ profile.role }}</li>
            <li>팔로우 팔로워</li>
          </ul>
        </div>
        <div class="ms-auto mt-auto">
          <el-button type="danger" @click="deleteMember()">회원탈퇴</el-button>
        </div>
      </div>

      <el-row class="profileButton d-flex justify-content-around mt-4">
        <button @click="this.$router.replace('/profilesettings')">
          <div class="d-flex flex-column align-items-center">
            <el-icon class="mb-2" :size="35">
              <Setting/>
            </el-icon>
            <div>프로필 설정</div>
          </div>
        </button>
        <button>
          <div class="d-flex flex-column align-items-center">
            <el-icon class="mb-2" :size="35">
              <ChatDotRound/>
            </el-icon>
            <div>대화방 보기</div>
          </div>
        </button>
      </el-row>
    </div>
  </div>
</template>
<script lang="ts" setup>
import Header from '@/components/Header.vue';
import { reactive, ref, toRefs } from "vue";
import { authStorage } from "@/stores/auth";
import axios from "axios";
import { useRouter } from "vue-router";

const route = useRouter();

const state = reactive({
  circleUrl:
      'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
  squareUrl:
      'https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png',
  sizeList: ['small', '', 'large'] as const,
})
const {circleUrl, squareUrl, sizeList} = toRefs(state)
const auth = authStorage();


const getProfile = auth.getProfile;
const profile = ref({
  nickname: String(getProfile.nickname),
  point: String(getProfile.point),
  role: String(getProfile.role),
});
const deleteMember = function () {
  axios.post("/my-backend-api/deletemember", {}).then(() => {
    auth.logout();
    alert("회원탈퇴가 완료되었습니다.");
    route.replace({name: "home"});
  }).catch(
      error => {
        console.log(error);
        alert("오류가 발생했습니다.");
        route.replace({name: "home"});
      }
  )
}
</script>

<style scoped lang="scss">
@import "../components/css/contentBase.css";

.demo-basic {
  text-align: center;
}

.demo-basic .sub-title {
  margin-bottom: 10px;
  font-size: 14px;
  color: var(--el-text-color-secondary);
}

.demo-basic .demo-basic--circle,
.demo-basic .demo-basic--square {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.demo-basic .block:not(:last-child) {
  border-right: 1px solid var(--el-border-color);
}

.demo-basic .block {
  flex: 1;
}

.demo-basic .el-col:not(:last-child) {
  border-right: 1px solid var(--el-border-color);
}

.simpleProfile ul {
  list-style: none;
  padding: 0;

  li {
    margin-bottom: 0.4rem;

    .title {
      a {
        font-size: 1.1rem;
        text-decoration: none;
        color: #383838;
      }

      &:hover {
        text-decoration: underline;
      }
    }

    .content {
      font-size: 0.85rem;
      margin-top: 8px;
      color: #7e7e7e;
    }

    &:last-child {
      margin-bottom: 0;
    }
  }
}

.simpleProfile {
  ul {
    li {
      font-size: 0.9rem;
    }
  }
}

.profileButton {
  .el-button {
    border: none;
  }

  button {
    border-radius: 3px;
    border: none;
    background: none;
    cursor: pointer;

  }
}
</style>