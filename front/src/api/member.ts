import apiController from '@/utils/ApiController';

export async function refreshToken() {
    try {
        await apiController.post('/refreshtoken');
    } catch (error) {
        return error;
    }
}