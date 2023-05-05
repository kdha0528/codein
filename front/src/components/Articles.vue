<template>
  <div class="content">
    <ul>
        <li v-for="article in articleList">
            <div class="d-flex">
                <div>
                    <div>
                        <div>프로필사진</div>
                        <div>닉네임</div>
                        <div>작성일자</div>
                    </div>
                    <div>
                        <div>좋아요</div>
                        <div>댓글수</div>
                        <div>조회수</div>
                    </div>
                </div>
                <div>
                    제목
                </div>
            </div>
            <el-divider/>
        </li>
    </ul>
  </div>
</template>
<script setup lang="ts">
import {ref} from "vue";

const articleList = ref({

})
</script>
<style scoped>
@import "../components/css/contentBase.css";

</style>