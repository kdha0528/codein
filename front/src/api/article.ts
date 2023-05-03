import apiController from "@/utils/ApiController";

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