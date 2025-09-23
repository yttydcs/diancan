<template>
  <div style="display: flex; justify-content: center; align-items: center; height: 100vh; background-color: #f0f2f5;">
    <n-card title="登录" style="width: 400px;">
      <n-form @submit.prevent="handleLogin">
        <n-form-item-row label="用户名">
          <n-input v-model:value="username" placeholder="请输入用户名" />
        </n-form-item-row>
        <n-form-item-row label="密码">
          <n-input type="password" v-model:value="password" placeholder="请输入密码" />
        </n-form-item-row>
        <n-button type="primary" block attr-type="submit">登录</n-button>
      </n-form>
    </n-card>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import api from '../api';
import store from '../store';

const username = ref('');
const password = ref('');
const router = useRouter();

const handleLogin = async () => {
  try {
    const userData = await api.post('/user/login', {
      username: username.value,
      password: password.value,
    });
    store.setUser(userData);
    router.push('/');
  } catch (error) {
    console.error(error);
  }
};

</script>
