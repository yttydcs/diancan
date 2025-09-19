<template>
  <n-layout style="height: 100vh">
    <n-layout-header bordered style="height: 64px; padding: 0 20px; display: flex; align-items: center; justify-content: space-between;">
      <div style="font-size: 20px; font-weight: bold;">点餐系统后台</div>
      <n-button @click="handleLogout">退出登录</n-button>
    </n-layout-header>
    <n-layout has-sider>
      <n-layout-sider
        bordered
        collapse-mode="width"
        :collapsed-width="64"
        :width="240"
        :collapsed="collapsed"
        show-trigger
        @collapse="collapsed = true"
        @expand="collapsed = false"
      >
        <n-menu
          v-model:value="activeKey"
          :collapsed="collapsed"
          :collapsed-width="64"
          :collapsed-icon-size="22"
          :options="menuOptions"
          @update:value="handleMenuSelect"
        />
      </n-layout-sider>
      <n-layout-content style="padding: 24px;">
        <router-view />
      </n-layout-content>
    </n-layout>
  </n-layout>
</template>

<script setup>
import { ref, h } from 'vue';
import { useRouter } from 'vue-router';
import { NIcon } from 'naive-ui';
import {
  RestaurantOutline as SeatIcon,
  FastFoodOutline as FoodIcon,
  ReceiptOutline as OrderIcon,
} from '@vicons/ionicons5';

function renderIcon(icon) {
  return () => h(NIcon, null, { default: () => h(icon) });
}

const router = useRouter();
const collapsed = ref(false);
const activeKey = ref('seat-management');

const menuOptions = [
  {
    label: '座位管理',
    key: 'seat-management',
    icon: renderIcon(SeatIcon),
  },
  {
    label: '菜单管理',
    key: 'food-management',
    icon: renderIcon(FoodIcon),
  },
  {
    label: '订单管理',
    key: 'order-management',
    icon: renderIcon(OrderIcon),
  },
];

const handleMenuSelect = (key) => {
  router.push({ name: key });
};

const handleLogout = () => {
    sessionStorage.removeItem('user');
    router.push('/login');
}
</script>
