<template>
    <div class=" pagination mt-4 pb-5" >
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
import {inject, onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {useResponseStore} from "@/stores/Response";
import {useArticlesStore} from "@/stores/articles";
import {useActivitiesStore} from "@/stores/activities";
import {usePageStore} from "@/stores/page";
const router = useRouter();
const route = useRoute();
const pageStore = usePageStore();

const currentPage = ref(1);
const maxPage = ref(1);
const pageSize = 20;

onMounted(()=>{
    currentPage.value = pageStore.getCurrentPage;
    if(pageStore.getMaxPage !== 0) {
        maxPage.value = pageStore.getMaxPage;
    }
})

const onPaging: any = inject('onPaging');

</script>
<style scoped>

.el-pagination{
    justify-content: space-between;
    --el-pagination-font-size: 1rem;
}

</style>