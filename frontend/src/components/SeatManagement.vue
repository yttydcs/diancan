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
import axios from 'axios';
import { useMessage, NButton, NSpace } from 'naive-ui';

const seats = ref([]);
const showModal = ref(false);
const isEdit = ref(false);
const currentSeat = ref({
  seatNumber: '',
  capacity: 1,
  storeId: null,
});
const storeOptions = ref([]);
const message = useMessage();

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
    const response = await axios.get('/api/seat');
    seats.value = response.data;
  } catch (error) {
    message.error('获取座位列表失败');
  }
};

const fetchStores = async () => {
  try {
    const response = await axios.get('/api/store');
    storeOptions.value = response.data.map(store => ({
      label: store.name,
      value: store.id,
    }));
  } catch (error) {
    message.error('获取店铺列表失败');
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
    message.error('请选择一个店铺');
    return;
  }
  try {
    if (isEdit.value) {
      await axios.put('/api/seat', currentSeat.value);
      message.success('座位更新成功');
    } else {
      await axios.post('/api/seat', currentSeat.value);
      message.success('座位创建成功');
    }
    showModal.value = false;
    fetchSeats();
  } catch (error) {
    message.error('操作失败');
  }
};

const handleDelete = async (id) => {
  try {
    await axios.delete(`/api/seat/${id}`);
    message.success('座位删除成功');
    fetchSeats();
  } catch (error) {
    message.error('座位删除失败');
  }
};

const generateQrCode = async (id) => {
  try {
    const response = await axios.get(`/api/seat/qrcode/${id}`);
    const qrCodeBase64 = response.data;
    const qrWindow = window.open("", "QRCode", "width=350,height=350");
    qrWindow.document.write('<img src="data:image/png;base64,' + qrCodeBase64 + '" />');
  } catch (error) {
    message.error('生成二维码失败');
  }
};

onMounted(() => {
  fetchSeats();
  fetchStores();
});
</script>
