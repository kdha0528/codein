<template>
    <div class="d-flex flex-column ps-3 pb-3 pe-3" style="border-radius: 10px; background-color: #dcdfe6">
        <h3>{{ articleStore.getIntro.category }}</h3>
        <span>{{ articleStore.getIntro.message }}</span>
    </div>
</template>
<script setup lang="ts">
import {useArticlesStore} from "@/stores/articles";

const articleStore = useArticlesStore();
</script>
<style>

</style>