import axios from 'axios'
import { refreshToken } from "@/api/member";

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

        return response;
    },

    async function (error) {
        console.log('error = ', error.config);
        const errorAPI = error.config;
        if (error.response.data.code === 'A001' && errorAPI.retry === undefined) {
            errorAPI.retry = true;
            await refreshToken();
            return axios(errorAPI);
        }
        return Promise.reject(error);
    }
);

export default apiController;