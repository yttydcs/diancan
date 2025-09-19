<template>
  <div>
    <h1>点餐系统后台</h1>
    <n-tabs type="line" animated>
      <n-tab-pane name="seat-management" tab="座位管理">
        <n-h2>座位列表</n-h2>
        <n-button @click="showAddModal = true" type="primary" style="margin-bottom: 20px;">添加座位</n-button>
        <n-data-table :columns="seatColumns" :data="seats" :pagination="false" :bordered="false" />
      </n-tab-pane>
      <n-tab-pane name="food-management" tab="菜单管理">
        <n-h2>菜单管理</n-h2>
        <p>菜单管理功能待实现。</p>
      </n-tab-pane>
      <n-tab-pane name="order-management" tab="订单管理">
        <n-h2>订单管理</n-h2>
        <p>订单管理功能待实现。</p>
      </n-tab-pane>
    </n-tabs>

    <!-- 添加座位 Modal -->
    <n-modal v-model:show="showAddModal">
      <n-card style="width: 600px" title="添加新座位" :bordered="false" size="huge" role="dialog" aria-modal="true">
        <n-form>
          <n-form-item label="座位号">
            <n-input v-model:value="newSeat.seatNumber" placeholder="输入座位号" />
          </n-form-item>
          <n-form-item label="容量">
            <n-input-number v-model:value="newSeat.capacity" :min="1" />
          </n-form-item>
        </n-form>
        <template #footer>
          <n-button @click="addSeat" type="primary">确认</n-button>
          <n-button @click="showAddModal = false">取消</n-button>
        </template>
      </n-card>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, onMounted, h } from 'vue';
import axios from 'axios';
import { NButton } from 'naive-ui';

const seats = ref([]);
const showAddModal = ref(false);
const newSeat = ref({
  seatNumber: '',
  capacity: 1,
  status: 0,
});

const seatColumns = [
  { title: 'ID', key: 'id' },
  { title: '座位号', key: 'seatNumber' },
  { title: '容量', key: 'capacity' },
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
      return h(
        NButton,
        {
          size: 'small',
          onClick: () => generateQrCode(row.id)
        },
        { default: () => '生成二维码' }
      )
    }
  }
];

const fetchSeats = async () => {
  try {
    const response = await axios.get('/api/seat');
    seats.value = response.data;
  } catch (error) {
    console.error('获取座位失败:', error);
  }
};

const addSeat = async () => {
  try {
    await axios.post('/api/seat', newSeat.value);
    showAddModal.value = false;
    fetchSeats(); // Refresh list
  } catch (error) {
    console.error('添加座位失败:', error);
  }
};

const generateQrCode = async (id) => {
  try {
    const response = await axios.get(`/api/seat/qrcode/${id}`);
    const qrCodeBase64 = response.data;
    const qrWindow = window.open("", "QRCode", "width=350,height=350");
    qrWindow.document.write('<img src="data:image/png;base64,' + qrCodeBase64 + '" />');
  } catch (error) {
    console.error('生成二维码失败:', error);
  }
};

onMounted(fetchSeats);
</script>
