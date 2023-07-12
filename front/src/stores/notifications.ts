import { defineStore } from "pinia";
import type { Notification } from "@/custom-types/notification";

export const useNotificationStore = defineStore("notifications", {
    state: () => ({
        notifications: [] as Notification[],
        noMore: false as boolean,
        loading: false as boolean,
    }),
    getters: {
        getNotifications: (state) => {
            return state.notifications;
        },
        isNoMore:(state) => {
            return state.noMore;
        },
        getSize:(state)=>{
            return state.notifications.length;
        },
        getLastId:(state)=>{
            return state.notifications[state.notifications.length-1].id;
        },
    },
    actions: {
        addNotification(n: Notification){
            this.notifications.push(n);
        },
        clean(){
            this.notifications = [];
            this.noMore = false;
            this.loading = false;
        },
        noMoreNotification(){
            this.noMore = true;
        },
        startLoading(){
            this.loading = true;
        },
        finishLoading(){
            this.loading = false;
        },
    },
    persist: {
        storage: sessionStorage,
    },
})
