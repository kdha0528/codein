import {defineStore} from 'pinia';
import {Pagination} from "@/custom-types/pagination";

export const usePageStore = defineStore("page", {
    state: () => ({
        articlesCurrentPage:1,
        articlesMaxPage: 1,
        commentsCurrentPage:1,
        commentsMaxPage:1,
        currentPaginationType: Pagination.Articles as Pagination,
    }),
    getters: {
        getArticlesCurrentPage: (state)=>{
            return state.articlesCurrentPage
        },
        getArticlesMaxPage: (state)=>{
            return state.articlesMaxPage
        },
        getCommentsCurrentPage: (state)=>{
            return state.commentsCurrentPage
        },
        getCommentsMaxPage: (state)=>{
            return state.commentsMaxPage
        },
        getCurrentPaginationType: (state)=>{
            return state.currentPaginationType
        },
    },
    actions: {
        setArticlesCurrentPage(page: number) {
            this.currentPaginationType = Pagination.Articles;
            this.articlesCurrentPage = page;
        },
        setArticlesMaxPage(max: number) {
            this.articlesMaxPage = max;
        },
        setCommentsCurrentPage(page: number) {
            this.currentPaginationType = Pagination.Comments;
            this.commentsCurrentPage = page;
        },
        setCommentsMaxPage(max: number) {
            this.commentsMaxPage = max;
        },
    },
    persist: {
        storage: sessionStorage,
    },
})
