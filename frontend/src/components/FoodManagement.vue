<template>
  <div>
    <n-h2>菜单管理</n-h2>
    <n-button @click="openAddModal" type="primary" style="margin-bottom: 20px;">添加菜品</n-button>
    <n-data-table :columns="columns" :data="foods" :pagination="false" :bordered="false" />

    <!-- 添加/编辑菜品 Modal -->
    <n-modal v-model:show="showModal">
      <n-card style="width: 600px" :title="isEdit ? '编辑菜品' : '添加新菜品'">
        <n-form>
          <n-form-item label="店铺">
            <n-select v-model:value="currentFood.storeId" :options="storeOptions" />
          </n-form-item>
          <n-form-item label="菜品名称">
            <n-input v-model:value="currentFood.name" />
          </n-form-item>
          <n-form-item label="描述">
            <n-input type="textarea" v-model:value="currentFood.description" />
          </n-form-item>
          <n-form-item label="价格">
            <n-input-number v-model:value="currentFood.price" :min="0" :precision="2" />
          </n-form-item>
          <n-form-item label="分类">
            <n-input v-model:value="currentFood.category" />
          </n-form-item>
          <n-form-item label="图片">
            <n-upload
              action="/api/file/upload"
              :default-upload="true"
              @finish="handleUploadFinish"
            >
              <n-button>上传图片</n-button>
            </n-upload>
            <n-image v-if="currentFood.imageUrl" :src="currentFood.imageUrl" width="100" style="margin-top: 10px;" />
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
import { useMessage, NButton, NSpace, NImage } from 'naive-ui';

const foods = ref([]);
const showModal = ref(false);
const isEdit = ref(false);
const currentFood = ref({
  name: '',
  description: '',
  price: 0,
  category: '',
  storeId: null,
  imageUrl: '',
});
const storeOptions = ref([]);
const message = useMessage();

const columns = [
  { title: 'ID', key: 'id' },
  { title: '菜品名称', key: 'name' },
  {
    title: '图片',
    key: 'imageUrl',
    render(row) {
      return h(NImage, { src: row.imageUrl, width: "50" });
    },
  },
  { title: '价格', key: 'price' },
  { title: '分类', key: 'category' },
  { title: '店铺ID', key: 'storeId' },
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

const fetchFoods = async () => {
  try {
    const response = await axios.get('/api/food');
    foods.value = response.data;
  } catch (error) {
    message.error('获取菜品列表失败');
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
  currentFood.value = { name: '', description: '', price: 0, category: '', storeId: null, imageUrl: '' };
  showModal.value = true;
};

const openEditModal = (food) => {
  isEdit.value = true;
  currentFood.value = { ...food };
  showModal.value = true;
};

const handleSubmit = async () => {
  if (!currentFood.value.storeId) {
    message.error('请选择一个店铺');
    return;
  }
  try {
    if (isEdit.value) {
      await axios.put('/api/food', currentFood.value);
      message.success('菜品更新成功');
    } else {
      await axios.post('/api/food', currentFood.value);
      message.success('菜品创建成功');
    }
    showModal.value = false;
    fetchFoods();
  } catch (error) {
    message.error('操作失败');
  }
};

const handleDelete = async (id) => {
  try {
    await axios.delete(`/api/food/${id}`);
    message.success('菜品删除成功');
    fetchFoods();
  } catch (error) {
    message.error('菜品删除失败');
  }
};

const handleUploadFinish = ({ file, event }) => {
  const responseBody = event.target.response;
  currentFood.value.imageUrl = responseBody;
};

onMounted(() => {
  fetchFoods();
  fetchStores();
});
</script>
