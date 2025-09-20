<template>
  <div>
    <n-h2>用户管理</n-h2>
    <n-button @click="showAddModal = true" type="primary" style="margin-bottom: 20px;">
      创建用户
    </n-button>
    <n-data-table :columns="columns" :data="users" />

    <!-- 创建用户 Modal -->
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
    
    <!-- 编辑用户 Modal -->
    <n-modal v-model:show="showEditModal">
        <n-card style="width: 600px" title="编辑用户">
            <n-form>
                <n-form-item label="用户名">
                    <n-input v-model:value="currentUser.username" />
                </n-form-item>
                <n-form-item label="角色">
                    <n-select v-model:value="currentUser.roleId" :options="roleOptions" />
                </n-form-item>
            </n-form>
            <template #footer>
                <n-button @click="handleUpdateUser">更新</n-button>
            </template>
        </n-card>
    </n-modal>

    <!-- 重置密码 Modal -->
    <n-modal v-model:show="showResetPasswordModal">
        <n-card style="width: 600px" title="重置密码">
            <n-form>
                <n-form-item label="新密码">
                    <n-input type="password" v-model:value="newPassword" />
                </n-form-item>
            </n-form>
            <template #footer>
                <n-button @click="handleResetPassword">确认</n-button>
            </template>
        </n-card>
    </n-modal>

  </div>
</template>

<script setup>
import { ref, onMounted, h } from 'vue';
import axios from 'axios';
import { useMessage, NButton, NSpace } from 'naive-ui';

const users = ref([]);
const showAddModal = ref(false);
const showEditModal = ref(false);
const showResetPasswordModal = ref(false);
const newUser = ref({ username: '', password: '', roleId: null });
const currentUser = ref({});
const newPassword = ref('');
const roleOptions = ref([]); 
const message = useMessage();

const columns = [
  { title: 'ID', key: 'id' },
  { title: '用户名', key: 'username' },
  { title: '角色ID', key: 'roleId' },
  {
    title: '操作',
    key: 'actions',
    render(row) {
      return h(NSpace, null, {
        default: () => [
          h(NButton, { size: 'small', onClick: () => openEditModal(row) }, { default: () => '编辑' }),
          h(NButton, { size: 'small', type: 'warning', onClick: () => openResetPasswordModal(row) }, { default: () => '重置密码' }),
          h(NButton, { size: 'small', type: 'error', onClick: () => handleDeleteUser(row.id) }, { default: () => '删除' }),
        ],
      });
    },
  },
];

const fetchUsers = async () => {
  try {
    const response = await axios.get('/api/user');
    users.value = response.data;
  } catch (error) {
    message.error('获取用户列表失败');
  }
};

const fetchRoles = async () => {
  try {
    const response = await axios.get('/api/role');
    roleOptions.value = response.data.map(role => ({ label: role.name, value: role.id }));
  } catch (error) {
    message.error('获取角色列表失败');
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
  }
};

const openEditModal = (user) => {
    currentUser.value = { ...user };
    showEditModal.value = true;
};

const handleUpdateUser = async () => {
    try {
        await axios.put('/api/user', currentUser.value);
        message.success('用户更新成功');
        showEditModal.value = false;
        fetchUsers();
    } catch (error) {
        message.error('用户更新失败');
    }
};

const openResetPasswordModal = (user) => {
    currentUser.value = user;
    newPassword.value = '';
    showResetPasswordModal.value = true;
};

const handleResetPassword = async () => {
    try {
        await axios.post(`/api/user/${currentUser.value.id}/reset-password`, { password: newPassword.value });
        message.success('密码重置成功');
        showResetPasswordModal.value = false;
    } catch (error) {
        message.error('密码重置失败');
    }
};

const handleDeleteUser = async (id) => {
    try {
        await axios.delete(`/api/user/${id}`);
        message.success('用户删除成功');
        fetchUsers();
    } catch (error) {
        message.error('用户删除失败');
    }
};

onMounted(() => {
  fetchUsers();
  fetchRoles();
});
</script>
