<template>
  <div class="user-management-container">
    <el-card class="user-management-card">
      <template #header>
        <div class="card-header">
          <h3>用户管理</h3>
          <el-button type="primary" @click="handleAddUser">
            <el-icon><Plus /></el-icon>
            新增用户
          </el-button>
        </div>
      </template>
      
      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-input v-model="searchForm.userName" placeholder="用户名" prefix-icon="Search" style="width: 200px; margin-right: 10px" />
        <el-select v-model="searchForm.status" placeholder="状态" style="width: 120px; margin-right: 10px">
          <el-option label="全部" value="" />
          <el-option label="启用" value="0" />
          <el-option label="禁用" value="1" />
        </el-select>
        <el-button type="primary" @click="handleSearch">
          <el-icon><Search /></el-icon>
          搜索
        </el-button>
        <el-button @click="resetSearch">
          重置
        </el-button>
      </div>
      
      <!-- 用户列表 -->
      <div class="user-table">
        <el-table
          v-loading="loading"
          :data="users"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" />
          <el-table-column prop="id" label="ID" width="60" />
          <el-table-column prop="userName" label="用户名" width="100" />
          <el-table-column prop="nickName" label="昵称" width="100" />
          <el-table-column prop="email" label="邮箱" min-width="200" />
          <el-table-column prop="status" label="状态" width="100">
            <template #default="scope">
              <el-tag :type="scope.row.status === 0 ? 'success' : 'danger'">
                {{ scope.row.status === 0 ? '启用' : '禁用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="250" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleEditUser(scope.row)">
                编辑
              </el-button>
              <el-button type="danger" size="small" @click="handleDeleteUser(scope.row.id)">
                删除
              </el-button>
              <el-button size="small" @click="handleAssignRoles(scope.row)">
                分配角色
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      
      <!-- 分页 -->
      <div class="pagination">
        <el-pagination
          v-model:current-page="currentPage"
          v-model:page-size="pageSize"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
    <!-- 新增/编辑用户对话框 -->
    <el-dialog
      v-model="userDialogVisible"
      :title="userDialogTitle"
      width="500px"
    >
      <el-form :model="userForm" :rules="userRules" ref="userFormRef" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="userForm.userName" placeholder="请输入用户名" :disabled="!!userForm.id" />
        </el-form-item>
        <el-form-item label="昵称" prop="nickName">
          <el-input v-model="userForm.nickName" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!userForm.id">
          <el-input v-model="userForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-switch v-model="userForm.status" active-value="0" inactive-value="1" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="userDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveUser">保存</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 分配角色对话框 -->
    <el-dialog
      v-model="roleDialogVisible"
      title="分配角色"
      width="500px"
    >
      <el-form>
        <el-form-item label="用户">
          <el-input v-model="selectedUser.userName" disabled />
        </el-form-item>
        <el-form-item label="角色">
          <el-checkbox-group v-model="selectedRoles">
            <el-checkbox v-for="role in roles" :key="role.id" :label="role.id">
              {{ role.roleName }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="roleDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveRoles">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { userApi, roleApi } from '../../services/api'
import { ElMessage } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'

// 表格数据
const loading = ref(false)
const users = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 搜索表单
const searchForm = reactive({
  userName: '',
  status: ''
})

// 选中的用户
const selectedUsers = ref([])

// 新增/编辑对话框
const userDialogVisible = ref(false)
const userDialogTitle = ref('新增用户')
const userFormRef = ref(null)
const userForm = reactive({
  id: '',
  userName: '',
  nickName: '',
  password: '',
  email: '',
  status: 1
})

const userRules = {
  userName: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  nickName: [
    { required: true, message: '请输入昵称', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少 6 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

// 分配角色对话框
const roleDialogVisible = ref(false)
const selectedUser = reactive({})
const selectedRoles = ref([])
const roles = ref([])

// 获取用户列表
const getUsers = async () => {
  loading.value = true
  try {
    const response = await userApi.getUsers(currentPage.value, pageSize.value)
    if (response.code === 200) {
      users.value = response.data.records || []
      total.value = response.data.total || 0
    }
  } catch (error) {
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 搜索用户
const handleSearch = () => {
  currentPage.value = 1
  getUsers()
}

// 重置搜索
const resetSearch = () => {
  searchForm.userName = ''
  searchForm.status = ''
  currentPage.value = 1
  getUsers()
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  getUsers()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  getUsers()
}

// 选择用户
const handleSelectionChange = (val) => {
  selectedUsers.value = val
}

// 新增用户
const handleAddUser = () => {
  userDialogTitle.value = '新增用户'
  Object.assign(userForm, {
    id: '',
    userName: '',
    nickName: '',
    password: '',
    email: '',
    status: 1
  })
  userDialogVisible.value = true
}

// 编辑用户
const handleEditUser = (user) => {
  userDialogTitle.value = '编辑用户'
  Object.assign(userForm, user)
  userDialogVisible.value = true
}

// 保存用户
const handleSaveUser = async () => {
  if (!userFormRef.value) return
  
  try {
    await userFormRef.value.validate()
    
    let response
    if (userForm.id) {
      // 更新用户
      response = await userApi.updateUser(userForm)
    } else {
      // 新增用户
      response = await userApi.addUser(userForm)
    }
    
    if (response.code === 200) {
      ElMessage.success(userForm.id ? '更新成功' : '新增成功')
      userDialogVisible.value = false
      getUsers()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    ElMessage.error('表单验证失败')
  }
}

// 删除用户
const handleDeleteUser = async (id) => {
  try {
    const response = await userApi.deleteUser(id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      getUsers()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

// 分配角色
const handleAssignRoles = async (user) => {
  selectedUser.userName = user.userName
  selectedUser.id = user.id
  
  // 获取所有角色
  try {
    const rolesResponse = await roleApi.getRoles()
    if (rolesResponse.code === 200) {
      // 检查是否是分页结果
      if (rolesResponse.data && rolesResponse.data.records) {
        roles.value = rolesResponse.data.records
      } else {
        roles.value = rolesResponse.data
      }
    }
  } catch (error) {
    ElMessage.error('获取角色列表失败')
  }
  
  // 获取用户现有角色
  try {
    const userRolesResponse = await userApi.getUserRoles(user.id)
    if (userRolesResponse.code === 200) {
      // 检查返回的数据类型
      const userRoles = userRolesResponse.data
      if (Array.isArray(userRoles)) {
        // 如果是字符串数组（roleKey列表），需要根据roleKey找到对应的角色ID
        if (userRoles.length > 0 && typeof userRoles[0] === 'string') {
          // 从所有角色中找出用户拥有的角色ID
          selectedRoles.value = roles.value
            .filter(role => userRoles.includes(role.roleKey))
            .map(role => role.id)
        } else {
          // 如果是角色对象数组，直接获取ID
          selectedRoles.value = userRoles.map(role => role.id)
        }
      }
    }
  } catch (error) {
    ElMessage.error('获取用户角色失败')
  }
  
  roleDialogVisible.value = true
}

// 保存角色分配
const handleSaveRoles = async () => {
  try {
    const response = await userApi.assignRoles(selectedUser.id, selectedRoles.value)
    if (response.code === 200) {
      ElMessage.success('分配角色成功')
      roleDialogVisible.value = false
    } else {
      ElMessage.error(response.message || '分配角色失败')
    }
  } catch (error) {
    ElMessage.error('分配角色失败')
  }
}

// 初始化数据
onMounted(() => {
  getUsers()
})
</script>

<style scoped>
.user-management-container {
  width: 100%;
}

.user-management-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.search-bar {
  margin: 20px 0;
  display: flex;
  align-items: center;
}

.user-table {
  margin-bottom: 20px;
}

.pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

@media (max-width: 768px) {
  .search-bar {
    flex-direction: column;
    align-items: stretch;
    gap: 10px;
  }
  
  .search-bar .el-input,
  .search-bar .el-select {
    width: 100% !important;
    margin-right: 0 !important;
  }
  
  .search-bar .el-button {
    width: 100%;
  }
}
</style>