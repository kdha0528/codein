import axios from 'axios'
import { refreshToken } from "@/controller/api/member";
import { useResponseStore } from "@/stores/Response";

const apiController = axios.create({
    baseURL: 'https://codein.loca.lt/my-backend-api',
    timeout: 60000,
});

apiController.interceptors.request.use(
    function (config) {
        return config;
    },
    function (error) {
        return Promise.reject(error);
    }
);

apiController.interceptors.response.use(

    async function (response) {
        if(await useResponseStore().getRetry) {
            await useResponseStore().setRetry(false);
        } else {
            await useResponseStore().setSuccess();
        }
        return response;
    }, async function (error) {
        const errorAPI = error.config;
        await console.log("error = ", error)
        await console.log("retry = ", errorAPI.retry)

        if(error.response === undefined){
            await console.log("ERROR RESPONSE IS UNDEFINED : "+error.message)
            await useResponseStore().setError(error.code, error.message);
            return await Promise.reject(error);
        } else if (error.response.data.code === 'A001' && errorAPI.retry === undefined) {
            errorAPI.retry = true;
            await useResponseStore().setRetry(true);
            await console.log("refresh token")
            await refreshToken();
            if(error.response.data.code !== 'A001'){
                await useResponseStore().setError(error.response.data.code, error.response.data.message);
            }
            return axios(errorAPI);
        } else {
            await useResponseStore().setError(error.response.data.code, error.response.data.message);
            return await Promise.reject(error);
        }
    }
);

export default apiController;