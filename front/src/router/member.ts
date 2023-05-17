import SignupView from '@/views/SignupView.vue';
import LoginView from "@/views/LoginView.vue";
import SettingsView from "@/views/SettingsView.vue";
import SettingsProfile from "@/components/member/SettingsProfile.vue";
import SettingsAccount from "@/components/member/SettingsAccount.vue";
import SettingsPassword from "@/components/member/SettingsPassword.vue";
import MembersView from "@/views/MembersView.vue";
import MemberActivities from "@/components/member/MemberActivities.vue";
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
                name: 'members-default',
                component: MemberActivities,
            },{
                path: 'articles',
                name: 'members-articles',
                component: MemberActivities,
            },{
                path: 'comments',
                name: 'members-comments',
                component: MemberActivities,
            },{
                path: 'liked_articles',
                name: 'members-liked-articles',
                component: MemberActivities,
            },{
                path: 'chatting_rooms',
                name: 'members-chatting-rooms',
                component: MemberActivities,
            }]
        },
]