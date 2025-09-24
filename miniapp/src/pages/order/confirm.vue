<template>
  <view class="container">
    <view class="order-details">
      <view v-for="item in cartItems" :key="item.id" class="order-item">
        <text>{{ item.name }}</text>
        <text>x{{ item.quantity }}</text>
        <text>¥{{ (item.price * item.quantity).toFixed(2) }}</text>
      </view>
    </view>
    <view class="total-price">
      <text>总计: ¥{{ totalPrice }}</text>
    </view>
    <button @click="submitOrder">提交订单</button>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue';
import { onLoad } from '@dcloudio/uni-app';

const cartItems = ref([]);
const totalPrice = ref(0);

onLoad(() => {
  const cart = JSON.parse(uni.getStorageSync('cart') || '{}');
  cartItems.value = Object.values(cart);
  totalPrice.value = cartItems.value.reduce((sum, item) => sum + item.price * item.quantity, 0).toFixed(2);
});

const submitOrder = async () => {
  const orderDTO = {
    seatId: cartItems.value[0].seatId, // Assuming all items are for the same seat
    storeId: cartItems.value[0].storeId,
    customerId: 1, // Placeholder for customer ID
    source: 0, // Customer self-order
    items: cartItems.value.map(item => ({
      foodId: item.id,
      quantity: item.quantity,
    })),
  };

  try {
    const res = await uni.request({
      url: '/api/order',
      method: 'POST',
      data: orderDTO,
    });
    
    if (res.data.status) {
      uni.showToast({ title: '下单成功' });
      uni.removeStorageSync('cart');
      // Navigate to order detail page
      uni.navigateTo({ url: `/pages/order/detail?orderId=${res.data.data.id}` });
    } else {
      uni.showToast({ title: res.data.message || '下单失败', icon: 'none' });
    }
  } catch (error) {
    uni.showToast({ title: '请求失败', icon: 'none' });
  }
};
</script>

<style>
.container { padding: 20px; }
.order-item { display: flex; justify-content: space-between; margin-bottom: 10px; }
.total-price { text-align: right; margin: 20px 0; }
</style>
