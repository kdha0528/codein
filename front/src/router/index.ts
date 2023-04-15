import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import SignupView from '../views/SignupView.vue';
import LoginView from "@/views/LoginView.vue";
import ProfileView from "@/views/ProfileView.vue";
import SettingsView from "@/views/SettingsView.vue";
import SettingsProfile from "@/components/SettingsProfile.vue";
import SettingsAccount from "@/components/SettingsAccount.vue";

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
        },
        {
            path: '/logout',
            name: 'logout',
            component: HomeView,
        },
        {
            path: '/settings',
            name: 'settings',
            component: SettingsView,
            children: [{
                path: '',
                name: 'profile',
                component: SettingsProfile,
            }, {
                path: 'profile',
                name: 'profile',
                component: SettingsProfile,
            }, {
                path: 'account',
                name: 'account',
                component: SettingsAccount,
            }]
        },
        {
            path: '/members/:id',
            name: 'members',
            component: ProfileView,
        },
    ],
});

export default router;
