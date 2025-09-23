import axios from 'axios';
import store from './store';

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

export const setupApiInterceptor = (message, router) => {
  api.interceptors.response.use(
    response => {
      const res = response.data;
      if (typeof res.status === 'boolean') {
        if (res.status) {
          if (res.message) { // Only show message if it exists
            message.success(res.message);
          }
          return res.data;
        } else {
          message.error(res.message || '操作失败');
          return Promise.reject(new Error(res.message || 'Error'));
        }
      }
      return response;
    },
    error => {
      if (error.response && error.response.status === 401) {
        store.setUser(null); // 清空用户状态
        router.push('/login');
        message.error('登录已过期，请重新登录');
      } else {
        message.error(error.response?.data?.message || error.message || '网络错误');
      }
      return Promise.reject(error);
    }
  );
};

export default api;
