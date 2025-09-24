<template>
  <view class="container">
    <view v-if="order">
      <text>订单号: {{ order.id }}</text>
      <text>状态: {{ statusText }}</text>
      <text>总价: ¥{{ order.totalPrice }}</text>
      <!-- More details here -->
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue';
import { onLoad } from '@dcloudio/uni-app';

const order = ref(null);

const statusMap = {
    0: '未支付',
    1: '已支付',
    2: '准备中',
    3: '已完成',
    4: '已取消',
};

const statusText = computed(() => {
    return order.value ? statusMap[order.value.status] : '';
});

onLoad(async (options) => {
  const orderId = options.orderId;
  if (orderId) {
    const res = await uni.request({ url: `/api/order/${orderId}` });
    if (res.data.status) {
      order.value = res.data.data;
    }
  }
});
</script>

<style>
.container { padding: 20px; }
</style>
