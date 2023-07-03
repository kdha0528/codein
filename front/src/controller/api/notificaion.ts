import apiController from "@/controller/ApiController";

export async function loadNotifications(path: string) {
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