import axios from 'axios'
import { refreshToken } from "@/api/member";
import { useResponseStore } from "@/stores/Response";

const apiController = axios.create({
    baseURL: 'https://codein.loca.lt/my-backend-api',
    timeout: 1000
});

apiController.interceptors.request.use(
    function (config) {
        console.log("request is fine")
        return config;
    },
    function (error) {
        console.log("request error")
        return Promise.reject(error);
    }
);

apiController.interceptors.response.use(

    async function (response) {
        console.log("response is fine")
        await useResponseStore().setResponseType(true);
        await useResponseStore().setErrorCode('');
        return response;
    }, async function (error) {
        const errorAPI = error.config;
        console.log("error code = ", error.response.data.code)
        if (error.response.data.code === 'A001' && errorAPI.retry === undefined) {
            errorAPI.retry = true;
            console.log('token invalid')
            await refreshToken();
            return await axios(errorAPI);
        }

        await useResponseStore().setResponseType(false);
        await useResponseStore().setErrorCode(error.response.data.code);
        return await Promise.reject(error);
    }
);

export default apiController;