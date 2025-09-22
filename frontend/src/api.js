import axios from 'axios';
import { useMessage } from 'naive-ui';

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

export const setupApiInterceptor = (message) => {
  api.interceptors.response.use(
    response => {
      const res = response.data;
      if (res.status) {
        if (res.message) {
          message.success(res.message);
        }
        return res.data;
      } else {
        message.error(res.message || '操作失败');
        return Promise.reject(new Error(res.message || 'Error'));
      }
    },
    error => {
      message.error(error.message || '网络错误');
      return Promise.reject(error);
    }
  );
};

export default api;
