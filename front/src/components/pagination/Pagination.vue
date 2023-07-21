<template>
    <div class=" pagination mt-4 pb-5" >
        <el-pagination
            layout="prev, pager, next"
            prev-text="Previous"
            next-text="Next"
            :page-size="pageSize"
            :total="maxPage*pageSize"
            v-model:current-page="currentPage"
            @click="onPaging(currentPage, componentName )"/>
    </div>
</template>
<script setup lang="ts">
import {getCurrentInstance, inject, onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";
import {usePageStore} from "@/stores/page";
import {Pagination} from "@/custom-types/pagination";
import { toRefs } from "vue";

const router = useRouter();
const route = useRoute();
const pageStore = usePageStore();
const componentName = ref(getCurrentInstance()?.parent?.type.__name);
const currentPage = ref(1);
const maxPage = ref(1);
const pageSize = 20;

onMounted(() => {
    if(componentName.value === 'Articles' || componentName.value === 'Activities'){
        currentPage.value = pageStore.getArticlesCurrentPage;
        if(pageStore.getArticlesMaxPage !== 0) {
            maxPage.value = pageStore.getArticlesMaxPage;
        }
    } else if (componentName.value === 'Comments'){
        currentPage.value = pageStore.getCommentsCurrentPage;
        if(pageStore.getCommentsMaxPage !== 0) {
            maxPage.value = pageStore.getCommentsMaxPage;
        }
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