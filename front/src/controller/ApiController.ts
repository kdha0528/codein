import axios from 'axios'
import { refreshToken } from "@/controller/api/member";
import { useResponseStore } from "@/stores/Response";
import { useAuthStore } from "@/stores/auth";

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
        if(useResponseStore().getRetry) {
            useResponseStore().setRetry(false);
        } else {
            useResponseStore().setSuccess();
        }
        return response;
    }, async function (error) {
        const errorAPI = error.config;
        console.log("error = ", error)

        if(error.response === undefined){   // error response 메세지가 undefinced인 에러인 경우
            console.log("ERROR RESPONSE IS UNDEFINED : "+error.message)
            useResponseStore().setError(error.code, error.message);
            return await Promise.reject(error);
        } else { // 일반적인 에러의 경우
            if(error.response.data.code === 'A001' && errorAPI.retry === undefined) {   // accesstoken이 null이어서 재발급 요청을 보내야하는 경우
                errorAPI.retry = true;
                useResponseStore().setRetry(true);
                console.log("refresh token")
                await refreshToken();
                if (error.response.data.code !== 'A001') {
                    useResponseStore().setError(error.response.data.code, error.response.data.message);
                }
                return axios(errorAPI);
            } else {
                useResponseStore().setError(error.response.data.code, error.response.data.message);
                return await Promise.reject(error);
            }
        }
    }
);

export default apiController;