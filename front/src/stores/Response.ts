import { defineStore } from 'pinia';
export const useResponseStore = defineStore("response", {
    state: () => ({
        type: true as boolean,
        errorCode: '',
    }),
    getters: {
        isError: (state) => {
            return !state.type;
        },
        getErrorCode: (state) =>{
            return state.errorCode;
        }
    },
    actions: {
        async setResponseType(type: boolean){
            this.type = type;
        },
        async setErrorCode(code:string){
            this.errorCode = code;
        },
    },
    persist: {
        storage: sessionStorage,
    },
})
