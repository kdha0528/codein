import apiController from '@/utils/ApiController';

export async function refreshToken() {
    try {
        await apiController.post('/refreshtoken');
    } catch (error) {
        return error;
    }
}

export async function signup(data: any) {
    try {
        return await apiController({
            url: '/signup',
            method: 'post',
            data: data,
        })
    } catch (error) {
        return error;
    }
}