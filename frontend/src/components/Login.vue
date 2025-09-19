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
import axios from 'axios';
import { useMessage } from 'naive-ui';

const username = ref('');
const password = ref('');
const router = useRouter();
const message = useMessage();


const handleLogin = async () => {
  if (!username.value || !password.value) {
    message.error('用户名和密码不能为空');
    return;
  }
  try {
    const response = await axios.post('/api/user/login', {
      username: username.value,
      password: password.value,
    });
    if (response.status === 200) {
      sessionStorage.setItem('user', username.value);
      message.success('登录成功');
      router.push('/');
    }
  } catch (error) {
    message.error('登录失败，请检查用户名和密码');
    console.error(error);
  }
};

</script>
