<template>
  <view class="container">
    <view class="store-info card">
      <text class="store-name">{{ store.name || '店铺' }}</text>
      <text class="store-seat">桌号: {{ seat.seatNumber || '-' }}</text>
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
          <text class="category-dot" />
          <text class="category-text">{{ category }}</text>
        </view>
      </scroll-view>

      <scroll-view scroll-y class="food-list" :scroll-into-view="activeCategoryId" @scroll="onFoodScroll">
        <view v-for="category in categories" :key="category" :id="'category-' + category" class="category-section">
          <view class="category-title">{{ category }}</view>

          <view v-for="food in foodsByCategory[category]" :key="food.id" class="food-item card">
            <image :src="toImageUrl(food.imageUrl)" class="food-image" mode="aspectFill" />
            <view class="food-details">
              <text class="food-name">{{ food.name }}</text>
              <text class="food-desc" v-if="food.description">{{ food.description }}</text>
              <view class="food-meta">
                <text class="food-price">¥{{ food.price }}</text>
                <view class="food-actions">
                  <button v-if="cart[food.id]" class="btn minus" @click="decrement(food)">-</button>
                  <text v-if="cart[food.id]" class="quantity">{{ cart[food.id].quantity }}</text>
                  <button class="btn plus" @click="increment(food)">+</button>
                </view>
              </view>
            </view>
          </view>
        </view>
      </scroll-view>
    </view>

    <view class="cart-bar safe-bottom">
      <text class="total">总计: ¥{{ totalPrice }}</text>
      <button class="checkout-btn" @click="goToConfirm">结算</button>
    </view>
  </view>
</template>

<script setup>
import { ref, computed } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { toImageUrl, apiUrl } from '../../utils/config';

const store = ref({});
const seat = ref({});
const foods = ref([]);
const cart = ref({});
const activeCategory = ref('');

onLoad(async (options) => {
  const seatId = options.seatId || 1; // For testing

  const seatRes = await uni.request({ url: apiUrl(`/api/seat/customer/${seatId}`) });
  seat.value = seatRes.data?.data || {};

  const storeRes = await uni.request({ url: apiUrl(`/api/store/customer/${seat.value.storeId}`) });
  store.value = storeRes.data?.data || {};

  const foodRes = await uni.request({ url: apiUrl(`/api/food/customer?storeId=${store.value.id}`) });
  foods.value = foodRes.data?.data || [];

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
    if (!result[key]) result[key] = [];
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
    if (cart.value[food.id].quantity === 0) delete cart.value[food.id];
  }
};

const goToConfirm = () => {
  uni.setStorageSync('cart', JSON.stringify(cart.value));
  uni.navigateTo({ url: '/pages/order/confirm' });
};

const activeCategoryId = computed(() => (activeCategory.value ? `category-${activeCategory.value}` : ''));

const scrollToCategory = (category) => {
  activeCategory.value = category;
};

const onFoodScroll = (e) => {
  // 可根据 e.detail.scrollTop 定位当前分类
};
</script>

<style>
/* 主题与基础样式 */
:root { --primary: #3b82f6; --bg: #f5f7fb; --card: #ffffff; --text: #111827; --muted: #6b7280; --border: #e5e7eb; }

.container { height: 100%; display: flex; flex-direction: column; overflow: hidden; background-color: var(--bg); padding: 24rpx; box-sizing: border-box; }

/* 扁平化卡片：去阴影与圆角，改用描边 */
.card { background: var(--card); border-radius: 0; box-shadow: none; border: 1rpx solid var(--border); }

.store-info { margin: 0; padding: 24rpx; display: flex; justify-content: space-between; align-items: baseline; }
.store-name { font-size: 36rpx; font-weight: 700; color: var(--text); }
.store-seat { font-size: 26rpx; color: var(--muted); }

.menu-content { flex: 1; display: flex; gap: 24rpx; padding: 0 0 24rpx; overflow: hidden; }

/* 左侧分类列表扁平化：去圆角阴影，改用描边 */
.category-list { width: 200rpx; height: 100%; background: var(--card); border-radius: 0; box-shadow: none; border: 1rpx solid var(--border); }
.category-item { display: flex; align-items: center; gap: 12rpx; padding: 22rpx 16rpx; color: var(--text); border-bottom: 1rpx solid var(--border); }
.category-item:last-child { border-bottom-width: 0; }
.category-item.active { background: #f0f6ff; border-left: 8rpx solid var(--primary); }
.category-dot { width: 12rpx; height: 12rpx; border-radius: 0; background: var(--primary); opacity: 0.6; }
.category-text { font-size: 26rpx; }

.food-list { flex: 1; height: 100%; }
.category-section { padding-top: 12rpx; }
.category-title { padding: 12rpx 8rpx; font-size: 28rpx; color: var(--muted); border-bottom: 1rpx solid var(--border); }

/* 菜品项扁平化：直角图片 + 卡片描边 */
.food-item { display: flex; padding: 20rpx; gap: 20rpx; margin-bottom: 20rpx; }
.food-image { width: 160rpx; height: 160rpx; border-radius: 0; background: #eee; }
.food-details { flex: 1; display: flex; flex-direction: column; }
.food-name { font-size: 30rpx; font-weight: 600; color: var(--text); }
.food-desc { margin-top: 8rpx; font-size: 24rpx; color: var(--muted); line-height: 1.5; max-height: 72rpx; overflow: hidden; }
.food-meta { margin-top: auto; display: flex; justify-content: space-between; align-items: center; }
.food-price { font-size: 32rpx; font-weight: 700; color: #ef4444; }
.food-actions { display: flex; align-items: center; gap: 12rpx; }
.quantity { min-width: 40rpx; text-align: center; color: var(--text); }

/* 扁平化按钮：直角、无阴影，可描边（修复小宽度下水平不居中） */
.btn {
  width: 56rpx;
  height: 56rpx;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  padding: 0;
  box-sizing: border-box;
  line-height: 1; /* 避免字体行高影响居中 */
  text-align: center;
  border-radius: 0;
  border: 1rpx solid var(--border);
  background: #ffffff;
  color: #374151;
  font-size: 32rpx; /* 提升可读性并更易居中 */
  font-weight: 600;
}
.btn.plus { background: var(--primary); color: #ffffff; border-color: var(--primary); }
/* 移除 uni-app 默认按钮伪元素边框，避免视觉偏移 */
.btn::after { border: none; }

/* 底部栏扁平化：去阴影，添加上边框 */
.cart-bar { display: flex; justify-content: space-between; align-items: center; padding: 20rpx 0 20rpx 24rpx; background: var(--card); box-shadow: none; border-top: 1rpx solid var(--border); margin-right: -8rpx; min-height: 96rpx; }
.safe-bottom { padding-bottom: constant(safe-area-inset-bottom); padding-bottom: env(safe-area-inset-bottom); }
.total { font-size: 30rpx; font-weight: 600; color: var(--text); }
.checkout-btn { background: var(--primary); color: white; /* 按钮内容垂直/水平居中 */ display: inline-flex; align-items: center; justify-content: center; /* 固定高度、去除行高影响 */ height: 72rpx; padding: 0 32rpx; line-height: normal; border-radius: 0; border: none; font-size: 28rpx; margin-left: auto; margin-right: 16rpx; }
/* 移除 uni-app 默认按钮伪元素边框（各端可能存在） */
.checkout-btn::after { border: none; }
</style>
