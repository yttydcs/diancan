import { createRouter, createWebHistory } from 'vue-router';
import axios from 'axios';
import Login from './components/Login.vue';
import Register from './components/Register.vue';
import AdminPanel from './components/AdminPanel.vue';

const routes = [
    { path: '/login', component: Login, name: 'Login' },
    { path: '/register', component: Register, name: 'Register' },
    { path: '/', component: AdminPanel, meta: { requiresAuth: true } },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// Axios 响应拦截器
axios.interceptors.response.use(response => {
    return response;
}, error => {
    if (error.response && error.response.status === 401) {
        // 如果是401，则重定向到登录页面
        router.push('/login');
    }
    return Promise.reject(error);
});


router.beforeEach((to, from, next) => {
    const loggedIn = sessionStorage.getItem('user');
    if (to.matched.some(record => record.meta.requiresAuth) && !loggedIn) {
        next('/login');
    } else {
        next();
    }
});

export default router;
