import {defineStore} from "pinia";

export const authStorage = defineStore('token', {
    state: () => ({
        email: null as string | null,
        accesstoken: null as string | null,
    }),
    getters: {
        isLoggedIn: (state) => {
            return state.accesstoken != null;
        },
    },
    actions: {
        login(email: string, token: any) {
            this.email = email;
            this.accesstoken = token;
        },
        logout() {
            this.email = null;
            this.accesstoken = null;
        }
    },
    persist: {
        storage: sessionStorage,
    }
});

