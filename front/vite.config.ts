import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueJsx from '@vitejs/plugin-vue-jsx'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [vue(), vueJsx()],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    server: {
        proxy: {
            // 서버에서 my-backend-api로 시작하지 않아도 프론트단에서 /my-backend-api/***으로 적고 여기서 /my-backend-api는 지우고 보낸다.
            // 이 방법으로 CORS 문제를 해결함과 동시에 경로까지 관리할 수 있게 되었다.
            '/my-backend-api/home': {
                target: 'http://localhost:8080',
                rewrite: (path) => path.replace(/^\/my-backend-api/, '')
            },
            '/my-backend-api/signup': {
                target: 'http://localhost:8080',
                rewrite: (path) => path.replace(/^\/my-backend-api/, '')
            },
            '/my-backend-api/login': {
                target: 'http://localhost:8080',
                rewrite: (path) => path.replace(/^\/my-backend-api/, '')
            },
            '/my-backend-api/logout': {
                target: 'http://localhost:8080',
                rewrite: (path) => path.replace(/^\/my-backend-api/, '')
            }, '/my-backend-api/getprofile': {
                target: 'http://localhost:8080',
                rewrite: (path) => path.replace(/^\/my-backend-api/, '')
            }, '/my-backend-api/editmember': {
                target: 'http://localhost:8080',
                rewrite: (path) => path.replace(/^\/my-backend-api/, '')
            }, '/my-backend-api/deletemember': {
                target: 'http://localhost:8080',
                rewrite: (path) => path.replace(/^\/my-backend-api/, '')
            },
        }
    }
})
