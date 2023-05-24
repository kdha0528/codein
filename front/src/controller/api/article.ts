import apiController from "@/controller/ApiController";

export async function getArticle(path: string) {
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

export async function getArticles(path: string) {
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

export async function createDummies() {
    try {
        const response = await apiController({
            url:  '/create-dummies',
            method: 'post',
        })
        return response.data;
    } catch (error) {
        return error;
    }
}

export async function write(data:any) {
    try {
        await apiController({
            url:  '/article/new',
            method: 'post',
            data: data,
        })
    } catch (error) {
        return error;
    }
}

export async function editArticle(path:any, data:any) {
    try {
       await apiController({
            url: path,
            method: 'post',
            data:data,
        })
    } catch (error) {
        return error;
    }
}
export async function deleteArticle(path:any) {
    try {
        await apiController({
            url: path,
            method: 'delete'
        })
    } catch (error) {
        return error;
    }
}
export async function likeArticle(id:any) {
    try {
        await apiController({
            url: '/articles/'+id+'/like',
            method: 'post'
        })
    } catch (error) {
        return error;
    }
}
