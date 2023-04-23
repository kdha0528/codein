import {defineStore} from "pinia";
export const useResponseStore = defineStore("auth", {
    state: () => ({
        type: true as boolean,
        errorCode: ''
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
        setResponseType(type: boolean){
            this.type = type;
        },
        setErrorCode(code:string){
            this.errorCode = code;
        }
    },
    persist: {
        storage: sessionStorage,
    },
})
