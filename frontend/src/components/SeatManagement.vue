<template>
  <div>
    <n-h2>座位管理</n-h2>
    <n-button @click="openAddModal" type="primary" style="margin-bottom: 20px;">添加座位</n-button>
    <n-data-table :columns="columns" :data="seats" :pagination="false" :bordered="false" />

    <!-- 添加/编辑座位 Modal -->
    <n-modal v-model:show="showModal">
      <n-card style="width: 600px" :title="isEdit ? '编辑座位' : '添加新座位'">
        <n-form>
          <n-form-item label="店铺">
            <n-select v-model:value="currentSeat.storeId" :options="storeOptions" />
          </n-form-item>
          <n-form-item label="座位号">
            <n-input v-model:value="currentSeat.seatNumber" placeholder="输入座位号" />
          </n-form-item>
          <n-form-item label="容量">
            <n-input-number v-model:value="currentSeat.capacity" :min="1" />
          </n-form-item>
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

const seats = ref([]);
const showModal = ref(false);
const isEdit = ref(false);
const currentSeat = ref({
  seatNumber: '',
  capacity: 1,
  storeId: null,
});
const storeOptions = ref([]);

const columns = [
  { title: 'ID', key: 'id' },
  { title: '座位号', key: 'seatNumber' },
  { title: '容量', key: 'capacity' },
  { title: '店铺ID', key: 'storeId' },
  {
    title: '状态',
    key: 'status',
    render(row) {
      const statusMap = { 0: '空闲', 1: '占用', 2: '已预约' };
      return statusMap[row.status] || '未知';
    }
  },
  {
    title: '操作',
    key: 'actions',
    render(row) {
      return h(NSpace, null, {
        default: () => [
          h(NButton, { size: 'small', onClick: () => openEditModal(row) }, { default: () => '编辑' }),
          h(NButton, { size: 'small', onClick: () => generateQrCode(row.id) }, { default: () => '二维码' }),
          h(NButton, { size: 'small', type: 'error', onClick: () => handleDelete(row.id) }, { default: () => '删除' }),
        ],
      });
    },
  },
];

const fetchSeats = async () => {
  try {
    seats.value = await api.get('/seat');
  } catch (error) {
    console.error(error);
  }
};

const fetchStores = async () => {
  try {
    const stores = await api.get('/store');
    storeOptions.value = stores.map(store => ({
      label: store.name,
      value: store.id,
    }));
  } catch (error) {
    console.error(error);
  }
};

const openAddModal = () => {
  isEdit.value = false;
  currentSeat.value = { seatNumber: '', capacity: 1, storeId: null };
  showModal.value = true;
};

const openEditModal = (seat) => {
  isEdit.value = true;
  currentSeat.value = { ...seat };
  showModal.value = true;
};

const handleSubmit = async () => {
  if (!currentSeat.value.storeId) {
    //  naive-ui message is not available here, use alert instead
    alert('请选择一个店铺');
    return;
  }
  try {
    if (isEdit.value) {
      await api.put('/seat', currentSeat.value);
    } else {
      await api.post('/seat', currentSeat.value);
    }
    showModal.value = false;
    fetchSeats();
  } catch (error) {
    console.error(error);
  }
};

const handleDelete = async (id) => {
  try {
    await api.delete(`/seat/${id}`);
    fetchSeats();
  } catch (error) {
    console.error(error);
  }
};

const generateQrCode = async (id) => {
  try {
    const qrCodeBase64 = await api.get(`/seat/qrcode/${id}`);
    const qrWindow = window.open("", "QRCode", "width=350,height=350");
    qrWindow.document.write(`<img src="data:image/png;base64,${qrCodeBase64}" />`);
  } catch (error) {
    console.error(error);
  }
};

onMounted(() => {
  fetchSeats();
  fetchStores();
});
</script>
