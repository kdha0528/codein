import { defineStore } from 'pinia';
import type {Article} from "@/components/custom-types/article";

export const useArticlesStore = defineStore("article", {
    state: () => ({
        articles: [] as Article[],
        maxPage: 1,
    }),
    getters: {
        getArticles: (state) => {
            return state.articles;
        },
        getMaxPage: (state)=>{
            return state.maxPage;
        }
    },
    actions: {
        addArticle(a: Article){
            this.articles.push(a);
        },
        setMaxPage(m: number){
            this.maxPage = m;
        },
        clean(){
            this.articles = [];
            this.maxPage = 1;
        },
    },
    persist: {
        storage: sessionStorage,
    },
})
