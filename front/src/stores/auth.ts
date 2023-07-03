import { defineStore } from 'pinia';
import type { Profile } from "@/custom-types/profile";

export const useAuthStore = defineStore("auth", {
    state: () => ({
        member: null as Profile | any,
        notificationCount: 0
    }),
    getters: {
        getId: (state) => {
            return state.member.id;
        },
        isLoggedIn: (state) => {
            return state.member != null;
        },
        getProfile: (state) => {
            return state.member;
        },
        getProfileImage: (state) => {
            if(state.member.imagePath === null) {
                return null;
            } else {
                return state.member.imagePath;
            }
        },
        isAdmin: (state)=>{
            if(state.member != null){
                return state.member.role === 'ADMIN';
            } else {
                return false;
            }
        },
        getNotificationCount: (state) => {
            return state.notificationCount;
        },
    },
    actions: {
        login(m: Profile) {
            this.member = m;
        },
        logout() {
            this.member = null;
        },
        setNotificationCount(count: number){
            this.notificationCount = count;
        }
    },
    persist: {
        storage: sessionStorage,
    },
})
