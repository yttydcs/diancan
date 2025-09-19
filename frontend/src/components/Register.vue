<template>
  <div>
    <h2>Register</h2>
    <n-form @submit.prevent="handleRegister">
      <n-form-item label="Username">
        <n-input v-model:value="username" />
      </n-form-item>
      <n-form-item label="Password">
        <n-input type="password" v-model:value="password" />
      </n-form-item>
      <n-button type="primary" attr-type="submit">Register</n-button>
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

const handleRegister = async () => {
  try {
    const response = await axios.post('/api/user/register', {
      username: username.value,
      password: password.value,
    });
    if (response.data === 'Register success') {
      router.push('/login');
    } else {
      alert('Registration failed');
    }
  } catch (error) {
    console.error(error);
    alert('Registration failed');
  }
};
</script>
