import {defineStore} from "pinia";
import type {Comment} from "@/custom-types/comment";

export const useCommentsStore = defineStore("comments", {
    state: () => ({
        comments: [] as Comment[],
    }),
    getters: {
        getComments: (state) => {
            return state.comments;
        },
        getCommentsSize: (state)=>{
            return state.comments.length;
        },
    },
    actions: {
        isLastChild(child:Comment){
            const index = this.comments.findIndex(c => c.id === child.id);
            const next = this.comments[index+1];
            let result = false;

            if(next.parentId === null) {
                result = true;
            }

            return result;
        },
        isLastComment(parent:Comment){
            return (this.comments.lastIndexOf(parent) === (this.comments.length-1));
        },
        addComments(c: Comment){
            this.comments.push(c);
        },
        clean(){
            this.comments = [];
        },
        hasChild(parent: Comment) {
            let response = false;
            this.comments.forEach((c:Comment)=>{
                if(c.parentId === parent.id) {
                    response = true;
                }
            });
            return response;
        }
    },
    persist: {
        storage: sessionStorage,
    },
})
