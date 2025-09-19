<template>
  <div>
    <h2>Login</h2>
    <n-form @submit.prevent="handleLogin">
      <n-form-item label="Username">
        <n-input v-model:value="username" />
      </n-form-item>
      <n-form-item label="Password">
        <n-input type="password" v-model:value="password" />
      </n-form-item>
      <n-button type="primary" attr-type="submit">Login</n-button>
    </n-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';

const username = ref('');
const password = ref('');
const router = useRouter();

const handleLogin = async () => {
  try {
    const response = await axios.post('/api/user/login', {
      username: username.value,
      password: password.value,
    });
    if (response.data === 'Login success') {
      sessionStorage.setItem('user', username.value);
      router.push('/');
    } else {
      alert('Login failed');
    }
  } catch (error) {
    console.error(error);
    alert('Login failed');
  }
};
</script>
