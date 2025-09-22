<template>
  <div>
    <n-h2>店铺管理</n-h2>
    <n-button @click="openAddModal" type="primary" style="margin-bottom: 20px;">
      创建店铺
    </n-button>
    <n-data-table :columns="columns" :data="stores" />

    <!-- 创建/编辑店铺 Modal -->
    <n-modal v-model:show="showModal">
      <n-card style="width: 600px" :title="isEdit ? '编辑店铺' : '创建新店铺'">
        <n-form>
          <n-form-item label="店铺名称">
            <n-input v-model:value="currentStore.name" />
          </n-form-item>
          <n-form-item label="地址">
            <n-input type="textarea" v-model:value="currentStore.address" />
          </n-form-item>
        </n-form>
        <template #footer>
          <n-button @click="handleSubmit">
            {{ isEdit ? '更新' : '创建' }}
          </n-button>
        </template>
      </n-card>
    </n-modal>

    <!-- 分配店长 Modal -->
    <n-modal v-model:show="showManagerModal">
        <n-card style="width: 600px" title="分配店长">
            <n-select
                v-model:value="selectedManagers"
                multiple
                :options="managerOptions"
                placeholder="选择店长"
            />
            <template #footer>
                <n-button @click="handleAssignManagers">确认分配</n-button>
            </template>
        </n-card>
    </n-modal>

  </div>
</template>

<script setup>
import { ref, onMounted, h } from 'vue';
import api from '../api';
import { NButton, NSpace } from 'naive-ui';

const stores = ref([]);
const showModal = ref(false);
const isEdit = ref(false);
const currentStore = ref({ name: '', address: '' });
const showManagerModal = ref(false);
const managerOptions = ref([]);
const selectedManagers = ref([]);

const columns = [
  { title: 'ID', key: 'id' },
  { title: '店铺名称', key: 'name' },
  { title: '地址', key: 'address' },
  {
    title: '操作',
    key: 'actions',
    render(row) {
      return h(NSpace, null, {
        default: () => [
          h(NButton, { size: 'small', onClick: () => openEditModal(row) }, { default: () => '编辑' }),
          h(NButton, { size: 'small', type: 'info', onClick: () => openManagerModal(row) }, { default: () => '分配店长' }),
          h(NButton, { size: 'small', type: 'error', onClick: () => handleDeleteStore(row.id) }, { default: () => '删除' }),
        ],
      });
    },
  },
];

const fetchStores = async () => {
  try {
    stores.value = await api.get('/store');
  } catch (error) {
    console.error(error);
  }
};

const fetchManagers = async () => {
    try {
        const users = await api.get('/user');
        managerOptions.value = users
            .filter(user => user.roleId === 2) // 假设 2 是 manager 的 roleId
            .map(user => ({ label: user.username, value: user.id }));
    } catch (error) {
        console.error(error);
    }
};

const openAddModal = () => {
  isEdit.value = false;
  currentStore.value = { name: '', address: '' };
  showModal.value = true;
};

const openEditModal = (store) => {
  isEdit.value = true;
  currentStore.value = { ...store };
  showModal.value = true;
};

const openManagerModal = async (store) => {
    currentStore.value = store;
    try {
        selectedManagers.value = await api.get(`/store/${store.id}/managers`);
        showManagerModal.value = true;
    } catch (error) {
        console.error(error);
    }
};

const handleSubmit = async () => {
  try {
    if (isEdit.value) {
      await api.put('/store', currentStore.value);
    } else {
      await api.post('/store', currentStore.value);
    }
    showModal.value = false;
    fetchStores();
  } catch (error) {
    console.error(error);
  }
};

const handleAssignManagers = async () => {
    try {
        await api.post(`/store/${currentStore.value.id}/managers`, selectedManagers.value);
        showManagerModal.value = false;
    } catch (error) {
        console.error(error);
    }
};

const handleDeleteStore = async (id) => {
  try {
    await api.delete(`/store/${id}`);
    fetchStores();
  } catch (error) {
    console.error(error);
  }
};

onMounted(() => {
    fetchStores();
    fetchManagers();
});
</script>
