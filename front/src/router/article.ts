import CategoryHeader from "@/components/article/CategoryHeader.vue";
import CategoryView from "@/views/CategoryView.vue";
import NewArticle from "@/views/NewArticle.vue";
import EditArticle from "@/views/EditArticle.vue";
import ArticleView from "@/views/ArticleView.vue";

export default [
    {
        path: '/',
        alias: ['','/'],
        name: 'index',
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
            path: '/question',
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
    },
    {
        path: '/:category/new',
        name: 'new-article',
        component: NewArticle,
        meta: {
            authRequire: true
        },
    },
    {
        path: '/articles/:id/edit',
        name: 'edit-article',
        component: EditArticle,
        meta: {
            authRequire: true
        },
    },
    {
        path: '/articles/:id',
        name: 'article',
        component: ArticleView,
    }
]
