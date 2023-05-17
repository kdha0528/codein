import apiController from '@/controller/ApiController';

export async function signup(data:any) {
    try {
        await apiController({
            url: '/signup',
            method: 'post',
            data: data,
        })
    } catch (error) {

        return error;
    }
}

export async function login(data: any) {
    try {
        const response = await apiController({
            url: '/login',
            method: 'post',
            data: data,
        })
        return response.data
    } catch (error) {
        return error;
    }
}

export async function logout() {
    try {
        await apiController({
            url: '/logout',
            method: 'post',
        })
    } catch (error) {
        return error;
    }
}

export async function getProfile() {
    try {
        const response =  await apiController({
            url: '/settings/profile',
            method: 'get',
        })
        return response.data
    } catch (error) {
        return error;
    }
}

export async function editProfile(data:any) {
    try {
        await apiController({
            url: '/settings/profile',
            method: 'post',
            headers: {
                'Content-Type': 'multipart/form-data',
            },
            data: data,
        })
    } catch (error) {
        return error;
    }
}

export async function getAccount() {
    try {
        const response = await apiController({
            url: '/settings/account',
            method: 'get',
        })
        return response.data
    } catch (error) {
        return error;
    }
}

export async function changePassword(data:any) {
    try {
        await apiController({
            url: '/settings/account/password',
            method: 'post',
            data: data,
        })
    } catch (error) {
        return error;
    }
}


export async function changeEmail(data:any) {
    try {
        await apiController({
            url: '/settings/account/email',
            method: 'post',
            data: data,
        })
    } catch (error) {
        return error;
    }
}

export async function changePhone(data:any) {
    try {
        await apiController({
            url: '/settings/account/phone',
            method: 'post',
            data: data,
        })
    } catch (error) {
        return error;
    }
}
export async function deleteAccount() {
    try {
        await apiController({
            url: '/settings/account',
            method: 'delete',
        })
    } catch (error) {
        return error;
    }
}

export async function getActivities(path: string) {
    try {
        const response = await apiController({
            url: path,
            method: 'get',
        })
        return response.data;
    } catch (error) {
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