import {createRouter, createWebHistory} from 'vue-router';
import HomeView from '../views/HomeView.vue';
import SignupView from '../views/SignupView.vue';
import LoginView from "@/views/LoginView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/home',
            name: 'home',
            component: HomeView,

        },
        {
            path: '/signup',
            name: 'signup',
            component: SignupView,
            //component: () => import('../views/AboutView.vue')
        },
        {
            path: '/login',
            name: 'login',
            component: LoginView,
        }, {
            path: '/logout',
            name: 'logout',
            component: HomeView,
        },
    ],
});

export default router;
