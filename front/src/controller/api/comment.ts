import apiController from "@/controller/ApiController";

export async function write(path: string, data:any) {
    try {
        await apiController({
            url:  path,
            method: 'post',
            data: data,
        })
    } catch (error) {
        return error;
    }
}

export async function likeComment(path: string) {
    try {
        await apiController({
            url: path,
            method: 'post'
        })
    } catch (error) {
        return error;
    }
}
export async function dislikeComment(path: string) {
    try {
        await apiController({
            url: path,
            method: 'post'
        })
    } catch (error) {
        return error;
    }
}