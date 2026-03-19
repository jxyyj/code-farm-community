<template>
  <div class="permission-management-container">
    <el-card class="permission-management-card">
      <template #header>
        <div class="card-header">
          <h3>权限管理</h3>
          <el-button type="primary" @click="handleAddPermission">
            <el-icon><Plus /></el-icon>
            新增权限
          </el-button>
        </div>
      </template>
      
      <!-- 权限列表 -->
      <div class="permission-table">
        <el-table
          v-loading="loading"
          :data="permissions"
          style="width: 100%"
          @selection-change="handleSelectionChange"
        >
          <el-table-column type="selection" width="50" />
          <el-table-column prop="id" label="ID" width="80" />
          <el-table-column prop="name" label="权限名称" />
          <el-table-column prop="permissionKey" label="权限代码" />
          <el-table-column prop="createdTime" label="创建时间" width="180" />
          <el-table-column label="操作" width="200" fixed="right">
            <template #default="scope">
              <el-button type="primary" size="small" @click="handleEditPermission(scope.row)">
                编辑
              </el-button>
              <el-button type="danger" size="small" @click="handleDeletePermission(scope.row.id)">
                删除
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
    
    <!-- 新增/编辑权限对话框 -->
    <el-dialog
      v-model="permissionDialogVisible"
      :title="permissionDialogTitle"
      width="500px"
    >
      <el-form :model="permissionForm" :rules="permissionRules" ref="permissionFormRef" label-width="100px">
        <el-form-item label="权限名称" prop="name">
          <el-input v-model="permissionForm.name" placeholder="请输入权限名称" />
        </el-form-item>
        <el-form-item label="权限代码" prop="permissionKey">
          <el-input v-model="permissionForm.permissionKey" placeholder="请输入权限代码" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="permissionForm.description" type="textarea" placeholder="请输入权限描述" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="permissionDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSavePermission">保存</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { permissionApi } from '../../services/api'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

// 表格数据
const loading = ref(false)
const permissions = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 选中的权限
const selectedPermissions = ref([])

// 新增/编辑对话框
const permissionDialogVisible = ref(false)
const permissionDialogTitle = ref('新增权限')
const permissionFormRef = ref(null)
const permissionForm = reactive({
  id: '',
  name: '',
  permissionKey: '',
  description: ''
})

const permissionRules = {
  name: [
    { required: true, message: '请输入权限名称', trigger: 'blur' }
  ],
  permissionKey: [
    { required: true, message: '请输入权限代码', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入权限描述', trigger: 'blur' }
  ]
}

// 获取权限列表
const getPermissions = async () => {
  loading.value = true
  try {
    console.log('开始获取权限列表...')
    const response = await permissionApi.getPermissions()
    console.log('获取权限列表响应:', response)
    console.log('响应数据类型:', typeof response)
    console.log('响应数据结构:', response)
    if (response.code === 200) {
      // 检查response.data是否是数组，如果不是，尝试获取其中的数组属性
      if (Array.isArray(response.data)) {
        console.log('权限数据是数组:', response.data)
        permissions.value = response.data
        total.value = response.data.length
      } else if (response.data && response.data.records) {
        console.log('权限数据在data.records属性中:', response.data.records)
        permissions.value = response.data.records
        total.value = response.data.total || response.data.records.length
      } else if (response.records) {
        console.log('权限数据在records属性中:', response.records)
        permissions.value = response.records
        total.value = response.total || response.records.length
      } else if (response.data && response.data.list) {
        console.log('权限数据在data.list属性中:', response.data.list)
        permissions.value = response.data.list
        total.value = response.data.list.length
      } else if (response.list) {
        console.log('权限数据在list属性中:', response.list)
        permissions.value = response.list
        total.value = response.list.length
      } else {
        console.error('权限数据格式不正确:', response)
        permissions.value = []
        total.value = 0
      }
    } else {
      console.error('获取权限列表失败，响应码:', response.code)
      ElMessage.error(response.message || '获取权限列表失败')
    }
  } catch (error) {
    console.error('获取权限列表失败:', error)
    ElMessage.error('获取权限列表失败')
  } finally {
    console.log('获取权限列表完成')
    loading.value = false
  }
}

// 分页处理
const handleSizeChange = (size) => {
  pageSize.value = size
  getPermissions()
}

const handleCurrentChange = (current) => {
  currentPage.value = current
  getPermissions()
}

// 选择权限
const handleSelectionChange = (val) => {
  selectedPermissions.value = val
}

// 新增权限
const handleAddPermission = () => {
  permissionDialogTitle.value = '新增权限'
  Object.assign(permissionForm, {
    id: '',
    permissionName: '',
    permissionCode: '',
    description: ''
  })
  permissionDialogVisible.value = true
}

// 编辑权限
const handleEditPermission = (permission) => {
  permissionDialogTitle.value = '编辑权限'
  Object.assign(permissionForm, permission)
  permissionDialogVisible.value = true
}

// 保存权限
const handleSavePermission = async () => {
  if (!permissionFormRef.value) return
  
  try {
    await permissionFormRef.value.validate()
    
    let response
    if (permissionForm.id) {
      // 更新权限
      response = await permissionApi.updatePermission(permissionForm)
    } else {
      // 新增权限
      response = await permissionApi.addPermission(permissionForm)
    }
    
    if (response.code === 200) {
      ElMessage.success(permissionForm.id ? '更新成功' : '新增成功')
      permissionDialogVisible.value = false
      getPermissions()
    } else {
      ElMessage.error(response.message || '操作失败')
    }
  } catch (error) {
    ElMessage.error('表单验证失败')
  }
}

// 删除权限
const handleDeletePermission = async (id) => {
  try {
    const response = await permissionApi.deletePermission(id)
    if (response.code === 200) {
      ElMessage.success('删除成功')
      getPermissions()
    } else {
      ElMessage.error(response.message || '删除失败')
    }
  } catch (error) {
    ElMessage.error('删除失败')
  }
}

// 初始化数据
onMounted(() => {
  getPermissions()
})
</script>

<style scoped>
.permission-management-container {
  width: 100%;
}

.permission-management-card {
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

.permission-table {
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