<template>
    <div class="pagination mt-4 pb-5" >
        <el-pagination
            layout="prev, pager, next"
            prev-text="Previous"
            next-text="Next"
            :page-size="pageSize"
            :total="maxPage*pageSize"
            v-model:current-page="currentPage"
            @click="onPaging(currentPage)"/>
    </div>
</template>
<script setup lang="ts">
import {inject, onMounted, ref, watch} from "vue";
import {useRouter} from "vue-router";
import {useResponseStore} from "@/stores/Response";
const router = useRouter();
const resStore = useResponseStore();

const currentPage = ref(1);
const maxPage = ref(1);
const pageSize = 20;

onMounted(()=>{
    const page:any = inject('page');
    if(typeof page.page === 'number'){
        currentPage.value = page.page;
        console.log("page = ", currentPage.value)
    }
    if(typeof page.max === 'number'){
        maxPage.value = page.max;
        console.log("max = ", maxPage.value)
    }
})
/*
watch(page, (newPage) => {
   currentPage.value = newPage.page;
   maxPage.value = newPage.max;
    console.log("max page = ", maxPage)
});*/

const onPaging: any = inject('onPaging');

</script>
<style scoped>
.el-pagination{
    justify-content: space-between;
    --el-pagination-font-size: 1rem;
}

</style>