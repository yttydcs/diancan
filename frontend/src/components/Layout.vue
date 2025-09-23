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
          :value="activeKey"
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
import { ref, h, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { NIcon } from 'naive-ui';
import store from '../store';
import {
  RestaurantOutline as SeatIcon,
  FastFoodOutline as FoodIcon,
  ReceiptOutline as OrderIcon,
  PeopleOutline as UserIcon,
  StorefrontOutline as StoreIcon,
} from '@vicons/ionicons5';

function renderIcon(icon) {
  return () => h(NIcon, null, { default: () => h(icon) });
}

const router = useRouter();
const route = useRoute();
const collapsed = ref(false);
const activeKey = computed(() => route.name);

const allMenuOptions = [
    {
        label: '用户管理',
        key: 'user-management',
        icon: renderIcon(UserIcon),
        permission: 'user:manage',
    },
    {
        label: '店铺管理',
        key: 'store-management',
        icon: renderIcon(StoreIcon),
        permission: 'store:manage',
    },
    {
        type: 'divider',
        key: 'd1'
    },
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

const menuOptions = computed(() => {
    return allMenuOptions.filter(option => {
        if (option.permission) {
            return store.hasPermission(option.permission);
        }
        return true;
    });
});

const handleMenuSelect = (key) => {
  router.push({ name: key });
};

const handleLogout = () => {
    store.setUser(null);
    router.push('/login');
}
</script>
