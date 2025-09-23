<template>
  <div>
    <n-h2>订单管理</n-h2>
    <n-button @click="openAddModal" type="primary" style="margin-bottom: 20px;">创建订单</n-button>
    <n-data-table :columns="columns" :data="orders" :pagination="false" :bordered="false" />

    <!-- 创建/编辑订单 Modal -->
    <n-modal v-model:show="showModal">
      <n-card style="width: 800px" :title="isEdit ? '编辑订单' : '创建新订单'">
        <n-form>
          <n-form-item label="店铺">
            <n-select v-model:value="currentOrder.storeId" :options="storeOptions" @update:value="handleStoreChange" />
          </n-form-item>
          <n-form-item label="座位">
            <n-select v-model:value="currentOrder.seatId" :options="seatOptions" />
          </n-form-item>
          <n-form-item label="顾客ID">
            <n-input-number v-model:value="currentOrder.customerId" />
          </n-form-item>
          <n-form-item label="订单来源">
             <n-select v-model:value="currentOrder.source" :options="sourceOptions" />
          </n-form-item>
          <n-form-item label="订单状态">
             <n-select v-model:value="currentOrder.status" :options="statusOptions" />
          </n-form-item>
          
          <n-h3>订单项</n-h3>
          <div v-for="(item, index) in currentOrder.items" :key="index" style="display: flex; align-items: center; margin-bottom: 10px;">
            <n-select v-model:value="item.foodId" :options="foodOptions" placeholder="选择菜品" style="flex: 2; margin-right: 10px;" />
            <n-input-number v-model:value="item.quantity" :min="1" placeholder="数量" style="flex: 1; margin-right: 10px;" />
            <n-button @click="removeItem(index)" type="error">-</n-button>
          </div>
          <n-button @click="addItem" type="primary">+</n-button>

        </n-form>
        <template #footer>
          <n-button @click="handleSubmit" type="primary">确认</n-button>
        </template>
      </n-card>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, onMounted, h } from 'vue';
import api from '../api';
import { NButton, NSpace } from 'naive-ui';

const orders = ref([]);
const showModal = ref(false);
const isEdit = ref(false);
const currentOrder = ref({
  storeId: null,
  seatId: null,
  customerId: null,
  source: 1, // 默认为店员下单
  status: 0, // 默认为未支付
  items: [],
});

const storeOptions = ref([]);
const seatOptions = ref([]);
const foodOptions = ref([]);
const sourceOptions = [
    { label: '顾客自主下单', value: 0 },
    { label: '店员代客下单', value: 1 },
];
const statusOptions = [
    { label: '未支付', value: 0 },
    { label: '已支付', value: 1 },
    { label: '准备中', value: 2 },
    { label: '已完成', value: 3 },
    { label: '已取消', value: 4 },
];

const columns = [
  { title: 'ID', key: 'id' },
  { title: '店铺ID', key: 'storeId' },
  { title: '座位ID', key: 'seatId' },
  { title: '总价', key: 'totalPrice' },
  { title: '状态', key: 'status' },
  {
    title: '操作',
    key: 'actions',
    render(row) {
      return h(NSpace, null, {
        default: () => [
          h(NButton, { size: 'small', onClick: () => openEditModal(row) }, { default: () => '编辑' }),
          h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row.id) }, { default: () => '删除' }),
        ],
      });
    },
  },
];

const fetchOrders = async () => {
  try {
    orders.value = await api.get('/order');
  } catch (error) {
    console.error(error);
  }
};

const fetchStores = async () => {
  try {
    const stores = await api.get('/store');
    storeOptions.value = stores.map(s => ({ label: s.name, value: s.id }));
  } catch (error) {
    console.error(error);
  }
};

const handleStoreChange = async (storeId) => {
    if (!storeId) {
        seatOptions.value = [];
        foodOptions.value = [];
        return;
    }
    try {
        const allSeats = await api.get('/seat');
        seatOptions.value = allSeats.filter(s => s.storeId === storeId).map(s => ({ label: s.seatNumber, value: s.id }));
        
        const allFoods = await api.get('/food');
        foodOptions.value = allFoods.filter(f => f.storeId === storeId).map(f => ({ label: f.name, value: f.id }));
    } catch (error) {
        console.error(error);
    }
};

const addItem = () => {
    currentOrder.value.items.push({ foodId: null, quantity: 1 });
};

const removeItem = (index) => {
    currentOrder.value.items.splice(index, 1);
};

const openAddModal = () => {
  isEdit.value = false;
  currentOrder.value = { storeId: null, seatId: null, customerId: null, source: 1, status: 0, items: [] };
  showModal.value = true;
};

const openEditModal = (order) => {
  isEdit.value = true;
  // 注意：编辑时，订单项需要单独查询
  currentOrder.value = { ...order, items: [] };
  showModal.value = true;
};

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await api.put(`/order/${currentOrder.value.id}`, currentOrder.value);
    } else {
      await api.post('/order', currentOrder.value);
    }
    showModal.value = false;
    fetchOrders();
  } catch (error) {
    console.error(error);
  }
};

const handleDelete = async (id) => {
  try {
    await api.delete(`/order/${id}`);
    fetchOrders();
  } catch (error) {
    console.error(error);
  }
};

onMounted(() => {
  fetchOrders();
  fetchStores();
});
</script>
