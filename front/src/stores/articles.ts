import { defineStore } from 'pinia';
import type {Article} from "@/custom-types/article";
import type {Intro} from "@/custom-types/intro";

export const useArticlesStore = defineStore("articles", {
    state: () => ({
        articles: [] as Article[],
        intro: {
          category: ' ',
          message: ' ',
        } as Intro,
    }),
    getters: {
        getArticles: (state) => {
            return state.articles;
        },
        getIntro: (state)=>{
            return state.intro;
        },
        introIsNull: (state) =>{
            return state.intro == null;
        },
    },
    actions: {
        addArticle(a: Article){
            this.articles.push(a);
        },
        setIntro(intro: Intro){
            this.intro = intro;
        },
        clean(){
            this.articles = [];
        },
    },
    persist: {
        storage: sessionStorage,
    },
})
