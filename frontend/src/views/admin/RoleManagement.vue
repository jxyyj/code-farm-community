<template>
  <div class="role-management-container">
    <el-card class="role-management-card">
      <template #header>
        <div class="card-header">
          <h3>角色管理</h3>
          <el-button type="primary" @click="handleAddRole">
            <el-icon><Plus /></el-icon>
            新增角色
          </el-button>
        </div>
      </template>
      
      <!-- 角色列表 -->
      <div class="role-table">
        <el-table
          v-loading="loading"
          :data="roles"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" />
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="roleName" label="角色名称" />
          <el-table-column prop="roleKey" label="角色键" />
          <el-table-column label="操作" width="300" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleEditRole(scope.row)">
                编辑
              </el-button>
              <el-button type="danger" size="small" @click="handleDeleteRole(scope.row.id)">
                删除
              </el-button>
              <el-button size="small" @click="handleAssignPermissions(scope.row)">
                分配权限
              </el-button>
              <el-button size="small" @click="handleRecycleRole(scope.row.id)">
                回收
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
    
    <!-- 新增/编辑角色对话框 -->
    <el-dialog
      v-model="roleDialogVisible"
      :title="roleDialogTitle"
      width="500px"
    >
      <el-form :model="roleForm" :rules="roleRules" ref="roleFormRef" label-width="80px">
        <el-form-item label="角色名称" prop="roleName">
          <el-input v-model="roleForm.roleName" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="角色键" prop="roleKey">
          <el-input v-model="roleForm.roleKey" placeholder="请输入角色键" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="roleDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSaveRole">保存</el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 分配权限对话框 -->
    <el-dialog
      v-model="permissionDialogVisible"
      title="分配权限"
      width="600px"
    >
      <el-form>
        <el-form-item label="角色">
          <el-input v-model="selectedRole.roleName" disabled />
        </el-form-item>
        <el-form-item label="权限">
          <el-checkbox-group v-model="selectedPermissions">
            <el-checkbox v-for="permission in permissions" :key="permission.id" :label="permission.id">
              {{ permission.permissionName }}
            </el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSavePermissions">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { roleApi, permissionApi } from '../../services/api'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

// 表格数据
const loading = ref(false)
const roles = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 选中的角色
const selectedRoles = ref([])

// 新增/编辑对话框
const roleDialogVisible = ref(false)
const roleDialogTitle = ref('新增角色')
const roleFormRef = ref(null)
const roleForm = reactive({
  id: '',
  roleName: '',
  roleKey: ''
})

const roleRules = {
  roleName: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ],
  roleKey: [
    { required: true, message: '请输入角色键', trigger: 'blur' }
  ]
}

// 分配权限对话框
const permissionDialogVisible = ref(false)
const selectedRole = reactive({})
const selectedPermissions = ref([])
const permissions = ref([])

// 获取角色列表
const getRoles = async () => {
  loading.value = true
  try {
    console.log('开始获取角色列表...')
    const response = await roleApi.getRoles()
    console.log('获取角色列表响应:', response)
    console.log('响应数据类型:', typeof response)
    console.log('响应数据结构:', response)
    if (response.code === 200) {
      // 检查response.data是否是数组，如果不是，尝试获取其中的数组属性
      if (Array.isArray(response.data)) {
        console.log('角色数据是数组:', response.data)
        roles.value = response.data
        total.value = response.data.length
      } else if (response.data && response.data.records) {
        console.log('角色数据在data.records属性中:', response.data.records)
        roles.value = response.data.records
        total.value = response.data.total || response.data.records.length
      } else if (response.records) {
        console.log('角色数据在records属性中:', response.records)
        roles.value = response.records
        total.value = response.total || response.records.length
      } else if (response.data && response.data.list) {
        console.log('角色数据在data.list属性中:', response.data.list)
        roles.value = response.data.list
        total.value = response.data.list.length
      } else if (response.list) {
        console.log('角色数据在list属性中:', response.list)
        roles.value = response.list
        total.value = response.list.length
      } else {
        console.error('角色数据格式不正确:', response)
        roles.value = []
        total.value = 0
      }
    } else {
      console.error('获取角色列表失败，响应码:', response.code)
      ElMessage.error(response.message || '获取角色列表失败')
    }
  } catch (error) {
    console.error('获取角色列表失败:', error)
    ElMessage.error('获取角色列表失败')
  } finally {
    console.log('获取角色列表完成')
    loading.value = false
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  getRoles()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  getRoles()
}

// 选择角色
const handleSelectionChange = (val) => {
  selectedRoles.value = val
}

// 新增角色
const handleAddRole = () => {
  roleDialogTitle.value = '新增角色'
  Object.assign(roleForm, {
    id: '',
    roleName: '',
    roleKey: ''
  })
  roleDialogVisible.value = true
}

// 编辑角色
const handleEditRole = (role) => {
  roleDialogTitle.value = '编辑角色'
  Object.assign(roleForm, role)
  roleDialogVisible.value = true
}

// 保存角色
const handleSaveRole = async () => {
  if (!roleFormRef.value) return
  
  try {
    await roleFormRef.value.validate()
    
    let response
    if (roleForm.id) {
      // 更新角色
      response = await roleApi.updateRole(roleForm)
    } else {
      // 新增角色
      response = await roleApi.addRole(roleForm)
    }
    
    if (response.code === 200) {
      ElMessage.success(roleForm.id ? '更新成功' : '新增成功')
      roleDialogVisible.value = false
      getRoles()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    ElMessage.error('表单验证失败')
  }
}

// 删除角色
const handleDeleteRole = async (id) => {
  try {
    const response = await roleApi.deleteRole(id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      getRoles()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

// 回收角色
const handleRecycleRole = async (id) => {
  try {
    const response = await roleApi.recycleRole(id)
    if (response.code === 200) {
      ElMessage.success('回收成功')
      getRoles()
    } else {
      ElMessage.error(response.message || '回收失败')
    }
  } catch (error) {
    ElMessage.error('回收失败')
  }
}

// 分配权限
const handleAssignPermissions = async (role) => {
  selectedRole.roleName = role.roleName
  selectedRole.id = role.id
  
  // 获取所有权限
  try {
    const permissionsResponse = await permissionApi.getPermissions()
    if (permissionsResponse.code === 200) {
      permissions.value = permissionsResponse.data
    }
  } catch (error) {
    ElMessage.error('获取权限列表失败')
  }
  
  // 获取角色现有权限
  try {
    const rolePermissionsResponse = await roleApi.getRolePermissions(role.id)
    if (rolePermissionsResponse.code === 200) {
      selectedPermissions.value = rolePermissionsResponse.data.map(permission => permission.id)
    }
  } catch (error) {
    ElMessage.error('获取角色权限失败')
  }
  
  permissionDialogVisible.value = true
}

// 保存权限分配
const handleSavePermissions = async () => {
  try {
    const response = await roleApi.grantPermissions(selectedRole.id, selectedPermissions.value)
    if (response.code === 200) {
      ElMessage.success('分配权限成功')
      permissionDialogVisible.value = false
    } else {
      ElMessage.error(response.message || '分配权限失败')
    }
  } catch (error) {
    ElMessage.error('分配权限失败')
  }
}

// 初始化数据
onMounted(() => {
  getRoles()
})
</script>

<style scoped>
.role-management-container {
  width: 100%;
}

.role-management-card {
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

.role-table {
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
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .card-header .el-button {
    width: 100%;
  }
}
</style>