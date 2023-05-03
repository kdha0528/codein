import apiController from "@/utils/ApiController";

export async function write() {
    try {
        const response = await apiController({
            url: '/home?page=1&size=5',
            method: 'post',
        })
        return response.data;
    } catch (error) {
        await console.log(error)

        return error;
    }
}