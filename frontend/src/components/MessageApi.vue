<template>
  <div></div>
</template>

<script setup>
import { useMessage } from 'naive-ui';
import { onMounted } from 'vue';
import { setupApiInterceptor } from '../api';

const message = useMessage();

onMounted(() => {
  setupApiInterceptor(message);
});
</script>
