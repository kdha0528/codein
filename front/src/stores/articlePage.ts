import { defineStore } from 'pinia';

export const useArticlePageStore = defineStore("article-page", {
    state: () => ({
        currentPage:1,
        maxPage: 1,
    }),
    getters: {
        getCurrentPage: (state)=>{
            return state.currentPage;
        },
        getMaxPage: (state)=>{
            return state.maxPage;
        },
    },
    actions: {
        setMaxPage(max: number){
            this.maxPage = max;
        },
        setCurrentPage(page: number){
            this.currentPage = page;
        },
    },
    persist: {
        storage: sessionStorage,
    },
})
