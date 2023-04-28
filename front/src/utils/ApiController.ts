import axios from 'axios'
import { refreshToken } from "@/api/member";
import { useResponseStore } from "@/stores/Response";

const apiController = axios.create({
    baseURL: 'https://codein.loca.lt/my-backend-api',
    timeout: 10000
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
        if(useResponseStore().getRetry) {
            await useResponseStore().setRetry(false);
        }else{
            await useResponseStore().setSuccess();
        }
        return response;
    }, async function (error) {
        const errorAPI = error.config;
        await console.log("error = ", error)
        if(error.response === undefined){
            await console.log(error.message)
            await useResponseStore().setError(false, error.code, error.message);
            return await Promise.reject(error);
        }
        if (error.response.data.code === 'A001' && errorAPI.retry === undefined) {
            errorAPI.retry = true;
            await useResponseStore().setRetry(true);
            await refreshToken();
            return await axios(errorAPI);
        }
        await useResponseStore().setError(false, error.response.data.code, error.response.data.message);
        return await Promise.reject(error);
    }
);

export default apiController;