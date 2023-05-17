import { defineStore } from 'pinia';
import type {Activity} from "@/components/custom-types/activity";

export const useActivitiesStore = defineStore("activity", {
    state: () => ({
        activities: [] as Activity[],
        maxPage: 1,
    }),
    getters: {
        getActivities: (state) => {
            return state.activities;
        },
        getMaxPage: (state)=>{
            return state.maxPage;
        },
    },
    actions: {
        addActivities(a: Activity){
            this.activities.push(a);
        },
        setMaxPage(m: number){
            this.maxPage = m;
        },
        clean(){
            this.activities = [];
            this.maxPage = 1;
        },
    },
    persist: {
        storage: sessionStorage,
    },
})
