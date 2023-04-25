import apiController from '@/utils/ApiController';
export async function getHome() {
    try {
        return await apiController({
            url: '/home?page=1&size=5',
            method: 'get',
        })
    } catch (error) {
        console.log("api member error here")
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
        console.log("api member error here")
        return error;
    }
}

export async function login(data: any) {
    try {
        return await apiController({
            url: '/login',
            method: 'post',
            data: data,
        })
    } catch (error) {
        console.log("api member error here")
        return error;
    }
}

export async function logout() {
    try {
        return await apiController({
            url: '/logout',
            method: 'post',
        })
    } catch (error) {
        console.log("api member error here")
        return error;
    }
}

export async function getProfile() {
    try {
        return await apiController({
            url: '/settings/profile',
            method: 'get',
        })
    } catch (error) {
        console.log("api member error here")
        return error;
    }
}
export async function edit(data: any) {
    try {
        return await apiController({
            url: '/settings/profile',
            method: 'post',
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            data: data,
        })
    } catch (error) {
        console.log("api member error here")
        return error;
    }
}

export async function refreshToken() {
    try {
        await apiController.post('/refreshtoken');
    } catch (error) {
        return error;
    }
}