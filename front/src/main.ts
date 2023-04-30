import { createApp } from 'vue';
import App from './App.vue';
import router from './router';
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import 'normalize.css';
import 'bootstrap/dist/css/bootstrap-utilities.css';
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate';
import { createPinia } from 'pinia';
import VueCookies from 'vue3-cookies';

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App);
app.use(VueCookies);
app.use(pinia);
app.use(router);
app.use(ElementPlus);

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.mount('#app');

