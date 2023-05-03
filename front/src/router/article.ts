import CategoryHeader from "@/components/CategoryHeader.vue";
import CategoryView from "@/views/CategoryView.vue";

export default [
    {
        path: '/',
        alias: ['', '/home'],
        name: 'category',
        component: CategoryView,
        children: [{
            path: '',
            name: 'community',
            component: CategoryHeader,
        },{
            path: '/community',
            name: 'community',
            component: CategoryHeader,
        },{
            path: '/questions',
            name: 'question',
            component: CategoryHeader,
        },{
            path: '/information',
            name: 'information',
            component: CategoryHeader,
        },{
            path: '/notice',
            name: 'notice',
            component: CategoryHeader,
        }]
    }
]
