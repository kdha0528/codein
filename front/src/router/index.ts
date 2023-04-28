import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '../views/HomeView.vue';
import SignupView from '../views/SignupView.vue';
import LoginView from "@/views/LoginView.vue";
import ProfileView from "@/views/ProfileView.vue";
import SettingsView from "@/views/SettingsView.vue";
import SettingsProfile from "@/components/SettingsProfile.vue";
import SettingsAccount from "@/components/SettingsAccount.vue";
import { useAuthStore } from "@/stores/auth";
import SettingsPassword from "@/components/SettingsPassword.vue";


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
            meta: {
                authRequire: true
            },
        },
        {
            path: '/settings',
            name: 'settings',
            component: SettingsView,
            meta: {
                authRequire: true
            },
            children: [{
                path: '',
                name: 'profile',
                component: SettingsProfile,
                meta: {
                    authRequire: true
                },
            }, {
                path: 'profile',
                name: 'profile',
                component: SettingsProfile,
                meta: {
                    authRequire: true
                },
            }, {
                path: 'account',
                name: 'account',
                component: SettingsAccount,
                meta: {
                    authRequire: true
                },
            },]
        }, {
            path: '/settings/account/password',
            name: 'password',
            component: SettingsPassword,
            meta: {
                authRequire: true
            },
        },
        {
        path: '/members/:id',
        name: 'members',
        component: ProfileView,
        meta: {
            authRequire: true
        },
        },
    ],
});

export default router;

router.beforeEach(function (to, from, next) {
    const auth = useAuthStore();

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
