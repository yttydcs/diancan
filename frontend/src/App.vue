<template>
  <n-config-provider>
    <n-message-provider>
      <message-api />
      <router-view></router-view>
    </n-message-provider>
  </n-config-provider>
</template>

<script setup>
import { NConfigProvider, NMessageProvider } from 'naive-ui';
import MessageApi from './components/MessageApi.vue';
</script>
