import { defineStore } from 'pinia';
export const useResponseStore = defineStore("response", {
    state: () => ({
        state: true as boolean,
        errorCode: '',
        errorMessage:'',
        retry: false as boolean,
    }),
    getters: {
        isError: (state) => {
            return !state.state;
        },
        isOK: (state) => {
            return state.state;
        },
        getErrorCode: (state) =>{
            return state.errorCode;
        },
        getErrorMessage: (state) =>{
            return state.errorMessage;
        }, getRetry: (state) =>{
            return state.retry;
        },
    },
    actions: {
        setError(state: boolean, code: string, message: string){
            this.state = state;
            this.errorCode = code;
            this.errorMessage = message;
        },
        setSuccess(){
            this.state = true;
            this.errorCode = '';
            this.errorMessage = '';
        },
        setRetry(retry: boolean){
            this.retry = retry;
        },
    },
    persist: {
        storage: sessionStorage,
    },
})
