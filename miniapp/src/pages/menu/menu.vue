<template>
  <view class="container">
    <view class="store-info">
      <text class="store-name">{{ store.name }}</text>
      <text>桌号: {{ seat.seatNumber }}</text>
    </view>
    
    <view class="menu-content">
      <scroll-view scroll-y class="category-list">
        <view 
          v-for="category in categories" 
          :key="category" 
          class="category-item"
          :class="{ active: activeCategory === category }"
          @click="scrollToCategory(category)"
        >
          {{ category }}
        </view>
      </scroll-view>
      
      <scroll-view scroll-y class="food-list" @scroll="onFoodScroll">
        <view v-for="category in categories" :key="category" :id="'category-' + category">
          <view class="category-title">{{ category }}</view>
          <view v-for="food in foodsByCategory[category]" :key="food.id" class="food-item">
            <image :src="toImageUrl(food.imageUrl)" class="food-image" />
            <view class="food-details">
              <text class="food-name">{{ food.name }}</text>
              <text class="food-price">¥{{ food.price }}</text>
            </view>
            <view class="food-actions">
              <button v-if="cart[food.id]" @click="decrement(food)">-</button>
              <text v-if="cart[food.id]">{{ cart[food.id].quantity }}</text>
              <button @click="increment(food)">+</button>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <view class="cart-bar">
      <text>总计: ¥{{ totalPrice }}</text>
      <button @click="goToConfirm">去结算</button>
    </view>
  </view>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { toImageUrl, apiUrl } from '../../utils/config';

const store = ref({});
const seat = ref({});
const foods = ref([]);
const cart = ref({});
const activeCategory = ref('');

onLoad(async (options) => {
  const seatId = options.seatId || 1; //  For testing

  //  Fetch seat info, then store info, then food info
  const seatRes = await uni.request({ url: apiUrl(`/api/seat/customer/${seatId}`) });
  seat.value = seatRes.data.data;

  const storeRes = await uni.request({ url: apiUrl(`/api/store/customer/${seat.value.storeId}`) });
  store.value = storeRes.data.data;

  const foodRes = await uni.request({ url: apiUrl(`/api/food/customer?storeId=${store.value.id}`) });
  foods.value = foodRes.data.data || [];

  if (categories.value.length > 0) {
    activeCategory.value = categories.value[0];
  }
});

const categories = computed(() => {
  return [...new Set((foods.value || []).map(f => f.category || '未分类'))];
});

const foodsByCategory = computed(() => {
  const result = {};
  for (const food of (foods.value || [])) {
    const key = food.category || '未分类';
    if (!result[key]) {
      result[key] = [];
    }
    result[key].push(food);
  }
  return result;
});

const totalPrice = computed(() => {
  return Object.values(cart.value).reduce((sum, item) => sum + item.price * item.quantity, 0).toFixed(2);
});

const increment = (food) => {
  if (!cart.value[food.id]) {
    cart.value[food.id] = { ...food, quantity: 0 };
  }
  cart.value[food.id].quantity++;
};

const decrement = (food) => {
  if (cart.value[food.id] && cart.value[food.id].quantity > 0) {
    cart.value[food.id].quantity--;
    if (cart.value[food.id].quantity === 0) {
      delete cart.value[food.id];
    }
  }
};

const goToConfirm = () => {
  uni.setStorageSync('cart', JSON.stringify(cart.value));
  uni.navigateTo({ url: '/pages/order/confirm' });
};

//  Scroll-related logic would go here
const scrollToCategory = (category) => {
    activeCategory.value = category;
    uni.pageScrollTo({
        selector: `#category-${category}`,
        duration: 300
    });
};

const onFoodScroll = (e) => {
    //  Logic to update activeCategory based on scroll position
};

</script>

<style>
/* Add some basic styling */
.container { display: flex; flex-direction: column; height: 100vh; }
.store-info { padding: 10px; }
.menu-content { display: flex; flex: 1; }
.category-list { width: 100px; background-color: #f8f8f8; }
.category-item { padding: 15px 10px; text-align: center; }
.category-item.active { background-color: #fff; }
.food-list { flex: 1; }
.category-title { padding: 10px; font-weight: bold; }
.food-item { display: flex; padding: 10px; }
.food-image { width: 80px; height: 80px; }
.food-details { flex: 1; margin-left: 10px; }
.food-actions { display: flex; align-items: center; }
.cart-bar { display: flex; justify-content: space-between; align-items: center; padding: 10px; border-top: 1px solid #eee; }
</style>
