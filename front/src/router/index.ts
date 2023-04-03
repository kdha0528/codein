import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import SignupView from '../views/SignupView.vue';
import LoginView from "@/views/LoginView.vue";
import ProfileView from "@/views/ProfileView.vue";
import ProfileSettings from "@/views/ProfileSettings.vue";

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
        }, {
            path: '/profile',
            name: 'profile',
            component: ProfileView,
        }, {
            path: '/profilesettings',
            name: 'profileSettings',
            component: ProfileSettings,
        },
    ],
});

export default router;
