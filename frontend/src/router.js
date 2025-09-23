import { createRouter, createWebHistory } from 'vue-router';
import api from './api';
import store from './store';
import Layout from './components/Layout.vue';
import Login from './components/Login.vue';
import SeatManagement from './components/SeatManagement.vue';
import FoodManagement from './components/FoodManagement.vue';
import OrderManagement from './components/OrderManagement.vue';
import UserManagement from './components/UserManagement.vue';
import StoreManagement from './components/StoreManagement.vue';

const routes = [
    { path: '/login', component: Login, name: 'Login' },
    {
        path: '/',
        component: Layout,
        meta: { requiresAuth: true },
        children: [
            { path: '', redirect: '/user-management' },
            { path: 'user-management', name: 'user-management', component: UserManagement },
            { path: 'store-management', name: 'store-management', component: StoreManagement },
            { path: 'seat-management', name: 'seat-management', component: SeatManagement },
            { path: 'food-management', name: 'food-management', component: FoodManagement },
            { path: 'order-management', name: 'order-management', component: OrderManagement },
        ]
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// The interceptor is now in api.js and initialized in MessageApi.vue
// No need for it here anymore.

router.beforeEach((to, from, next) => {
    if (to.matched.some(record => record.meta.requiresAuth) && !store.user) {
        next({ name: 'Login' });
    } else {
        next();
    }
});

export default router;
