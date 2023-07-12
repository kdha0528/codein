import {defineStore} from "pinia";
import type {Comment} from "@/custom-types/comment";

export const useCommentsStore = defineStore("comments", {
    state: () => ({
        parents: [] as Comment[],
        children:[] as Comment[],
    }),
    getters: {
        getParents: (state) => {
            return state.parents;
        },
        getParentsSize: (state)=>{
            return state.parents.length;
        },
    },
    actions: {
        getChildren(parentId: number) {
            const children = [] as Comment[];
            this.children.forEach((c: Comment) => {
                if(c.parentId === parentId){
                    children.push(c);
                }
            })
            return children;
        },
        getChildrenSize(parentId: Number) {
            let size = 0;
            this.children.forEach((c: Comment) => {
                if(c.parentId === parentId){
                    size++;
                }
            })
            return size;
        },
        isLastParent(parentId:number) {
            let index = 0;
            let seq = 0;
            this.parents.forEach((p: Comment) =>{
                index++;
                if(p.id === parentId){
                  seq = index;
                }
            })
            return this.getParentsSize === seq;
        },
        isLastChild(child:Comment){
            let index = 0;
            let seq = 0;
            this.children.forEach((c:Comment)=>{
                if(c.parentId === child.parentId) {
                    index++;
                    if(c.id === child.id){
                       seq = index;
                    }
                }
            })
            return this.getChildrenSize(child.parentId) === seq;
        },
        addComments(c: Comment){
            if(c.parentId === null) {
                this.parents.push(c);
            } else {
                this.children.push(c)
            }
        },
        clean(){
            this.parents = [];
            this.children = [];
        },
        hasChild(parentId: number):boolean{
            this.children.forEach((c:Comment)=>{
                if(c.parentId === parentId) {
                    return true;
                }
            })
            return false;
        }
    },
    persist: {
        storage: sessionStorage,
    },
})
