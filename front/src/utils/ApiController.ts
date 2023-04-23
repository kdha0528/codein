import axios from 'axios'
import { refreshToken } from "@/api/member";
import { useResponseStore } from "@/stores/Response";

const apiController = axios.create({
    baseURL: 'https://codein.loca.lt/my-backend-api',
    timeout: 1000
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
    function (response) {
        const resType = useResponseStore();
        resType.setResponseType(true);
        return response;
    }, async function (error) {
        const errorAPI = error.config;
        const resType = useResponseStore();
        console.log("error code = ", error.response.data.code)
        if (error.respons.data.code === 'A001' && errorAPI.retry === undefined) {
            errorAPI.retry = true;
            console.log('token invalid')
            await refreshToken();
            return await axios(errorAPI);
        }
        console.log("----")

        resType.setResponseType(false);
        resType.setErrorCode(error.response.data.code);
        return Promise.reject(error);
    }
);

export default apiController;