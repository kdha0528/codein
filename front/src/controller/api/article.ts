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

export async function edit(data:any, id:any) {
    try {
       await apiController({
            url: '/article/edit',
            method: 'post',
            data:[data,id]
        })
    } catch (error) {
        return error;
    }
}


