<template>
  <div>
    <n-h2>用户管理</n-h2>
    <n-button @click="showAddModal = true" type="primary" style="margin-bottom: 20px;">
      创建用户
    </n-button>
    <n-data-table :columns="columns" :data="users" />

    <n-modal v-model:show="showAddModal">
      <n-card style="width: 600px" title="创建新用户">
        <n-form>
          <n-form-item label="用户名">
            <n-input v-model:value="newUser.username" />
          </n-form-item>
          <n-form-item label="密码">
            <n-input type="password" v-model:value="newUser.password" />
          </n-form-item>
           <n-form-item label="角色">
            <n-select v-model:value="newUser.roleId" :options="roleOptions" />
          </n-form-item>
        </n-form>
        <template #footer>
          <n-button @click="handleCreateUser">创建</n-button>
        </template>
      </n-card>
    </n-modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import axios from 'axios';
import { useMessage } from 'naive-ui';

const users = ref([]);
const showAddModal = ref(false);
const newUser = ref({
  username: '',
  password: '',
  roleId: null,
});
const roleOptions = ref([]); 
const message = useMessage();

const columns = [
  { title: 'ID', key: 'id' },
  { title: '用户名', key: 'username' },
  { title: '角色ID', key: 'roleId' },
];

const fetchUsers = async () => {
  try {
    const response = await axios.get('/api/user');
    users.value = response.data;
  } catch (error) {
    message.error('获取用户列表失败');
    console.error(error);
  }
};

const fetchRoles = async () => {
  try {
    const response = await axios.get('/api/role');
    roleOptions.value = response.data.map(role => ({
      label: role.name,
      value: role.id,
    }));
  } catch (error) {
    message.error('获取角色列表失败');
    console.error(error);
  }
};

const handleCreateUser = async () => {
  try {
    await axios.post('/api/user', newUser.value);
    message.success('用户创建成功');
    showAddModal.value = false;
    fetchUsers();
  } catch (error) {
    message.error('用户创建失败');
    console.error(error);
  }
};

onMounted(() => {
  fetchUsers();
  fetchRoles();
});
</script>
