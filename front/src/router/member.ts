import SignupView from '@/views/SignupView.vue';
import LoginView from "@/views/LoginView.vue";
import SettingsView from "@/views/SettingsView.vue";
import SettingsProfile from "@/components/SettingsProfile.vue";
import SettingsAccount from "@/components/SettingsAccount.vue";
import SettingsPassword from "@/components/SettingsPassword.vue";
import MembersView from "@/views/MembersView.vue";
import MemberActivities from "@/components/MemberActivities.vue";
import CategoryView from "@/views/CategoryView.vue";

export default [
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
            component: CategoryView,
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
            component: MembersView,
            children: [{
                path: '',
                name: 'articles',
                component: MemberActivities,
            },{
                path: 'articles',
                name: 'articles',
                component: MemberActivities,
            },{
                path: 'comments',
                name: 'comments',
                component: MemberActivities,
            },{
                path: 'liked-articles',
                name: 'liked-articles',
                component: MemberActivities,
            },{
                path: 'chatting-rooms',
                name: 'chatting-rooms',
                component: MemberActivities,
            }]
        }
]