import { defineStore } from 'pinia';
import type { Profile } from "@/components/custom-types/profile";

export const useAuthStore = defineStore("auth", {
    state: () => ({
        member: null as Profile | any
    }),
    getters: {
        isLoggedIn: (state) => {
            return state.member != null;
        },
        getProfile: (state) => {
            return state.member;
        },
    },
    actions: {
        login(m: Profile) {
            this.member = m;
        },
        logout() {
            this.member = null;
        }
    },
    persist: {
        storage: sessionStorage,
    },
})
