import {createRouter, createWebHistory} from "vue-router";
import memberRoutes from "@/router/member";
import articleRoutes from "@/router/article";
import {useAuthStore} from "@/stores/auth";
import {useResponseStore} from "@/stores/Response";
import {useCookies} from 'vue3-cookies';


const router = createRouter({

    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [...memberRoutes, ...articleRoutes],
    scrollBehavior(to, from, savedPosition) {
        if (to.hash) {
            return {
                el: to.hash,
                behavior: "smooth"
            };
        } else if (savedPosition) {
            return savedPosition;
        } else {
            return { left: 0, top: 0 };
        }
    }
});

export default router;

router.beforeEach(function (to, from, next) {
    const auth = useAuthStore();
    const resStore = useResponseStore();
    const countNewNotifications = useCookies().cookies.get('count_new_notifications');

    if(from.name){
        to.meta.previousRouteName = from.name;
    }

    if(parseInt(countNewNotifications) !== auth.getNotificationCount) {
        auth.setNotificationCount(parseInt(countNewNotifications));
    }

    if(resStore.getErrorCode === 'A002'){
        console.log("refresh token 만료");
        alert("Refresh Token is invalid.");
        auth.logout();
        resStore.setSuccess();
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
