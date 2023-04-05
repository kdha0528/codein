import { defineStore } from "pinia";
import type { Profile } from "@/components/props/profile";

export const authStorage = defineStore('token', {
    state: () => ({
        member: null as Profile | any,
        accesstoken: null as String | any,
    }),
    getters: {
        isLoggedIn: (state) => {
            return state.accesstoken != null;
        },
        getProfile: (state) => {
            return state.member;
        },
        getToken: (state) => {
            return state.accesstoken;
        }
    },
    actions: {
        login(m: Profile, token: any) {
            this.member = m;
            this.accesstoken = token;
        },
        logout() {
            this.member = null;
            this.accesstoken = null;
        }
    },
    persist: {
        storage: sessionStorage,
    }
});

