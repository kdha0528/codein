import { defineStore } from 'pinia';
import type {Activity} from "@/custom-types/activity";

export const useActivitiesStore = defineStore("activities", {
    state: () => ({
        activities: [] as Activity[],
    }),
    getters: {
        getActivities: (state) => {
            return state.activities;
        },
    },
    actions: {
        addActivities(a: Activity){
            this.activities.push(a);
        },
        clean(){
            this.activities = [];
        },
    },
    persist: {
        storage: sessionStorage,
    },
})
