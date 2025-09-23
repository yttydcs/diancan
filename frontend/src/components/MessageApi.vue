<template>
  <div></div>
</template>

<script setup>
import { useMessage } from 'naive-ui';
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { setupApiInterceptor } from '../api';

const message = useMessage();
const router = useRouter();

onMounted(() => {
  setupApiInterceptor(message, router);
});
</script>
