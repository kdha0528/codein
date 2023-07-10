import {createRouter, createWebHistory} from "vue-router";
import memberRoutes from "@/router/member";
import articleRoutes from "@/router/article";
import {useAuthStore} from "@/stores/auth";
import {useResponseStore} from "@/stores/Response";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [...memberRoutes, ...articleRoutes]
});

export default router;

router.beforeEach(function (to, from, next) {
    const auth = useAuthStore();
    const resStore = useResponseStore();
    if(from.name){
        to.meta.previousRouteName = from.name;
    }

    if(resStore.getErrorCode === 'A002'){
        alert("Refresh Token is invalid.");
        auth.logout();
        next('/login');
    }

    if (to.meta.authRequire) {
        if (auth.isLoggedIn) {
            next();
        } else {
            alert("로그인이 필요합니다.");
            next('/login');
        }
    } else {
        next();
    }
})
