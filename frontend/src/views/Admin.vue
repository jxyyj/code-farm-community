<template>
  <div class="admin-container">
    <el-container class="admin-layout">
      <!-- 侧边栏 -->
      <el-aside width="250px" class="admin-sidebar">
        <div class="sidebar-header">
          <h1>安全框架管理平台</h1>
          <p>Security Framework Management</p>
        </div>
        <el-menu
          :default-active="activeMenu"
          class="sidebar-menu"
          router
          @select="handleMenuSelect"
        >
          <el-menu-item index="/admin/dashboard">
            <template #icon>
              <el-icon><House /></el-icon>
            </template>
            <span>控制台</span>
          </el-menu-item>
          <el-menu-item index="/admin/users">
            <template #icon>
              <el-icon><User /></el-icon>
            </template>
            <span>用户管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/roles">
            <template #icon>
              <el-icon><Key /></el-icon>
            </template>
            <span>角色管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/permissions">
            <template #icon>
              <el-icon><Lock /></el-icon>
            </template>
            <span>权限管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/profile">
            <template #icon>
              <el-icon><UserFilled /></el-icon>
            </template>
            <span>个人中心</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <!-- 主内容区 -->
      <el-container class="admin-main">
        <!-- 顶部导航栏 -->
        <el-header class="admin-header">
          <div class="header-left">
            <el-button type="text" @click="toggleSidebar">
              <el-icon><Menu /></el-icon>
            </el-button>
            <h2>{{ currentPage }}</h2>
          </div>
          <div class="header-center">
            <iframe width="300" scrolling="no" height="30" frameborder="0" allowtransparency="true" src="https://i.tianqi.com?c=code&id=34&icon=1&site=12&lang=cn"></iframe>
          </div>
          <div class="header-right">
            <el-dropdown>
              <span class="user-info">
                <el-avatar :size="32" :src="userAvatar"></el-avatar>
                <span class="user-name">{{ userName }}</span>
                <el-icon class="el-icon--right"><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="handleLogout">
                    <el-icon><SwitchButton /></el-icon>
                    <span>退出登录</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <!-- 内容区域 -->
        <el-main class="admin-content">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'
import { House, User, Key, Lock, Menu, ArrowDown, SwitchButton, UserFilled } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const activeMenu = ref('/admin/dashboard')
const sidebarCollapsed = ref(false)

const currentPage = computed(() => {
  const path = route.path
  switch (path) {
    case '/admin/dashboard':
      return '控制台'
    case '/admin/users':
      return '用户管理'
    case '/admin/roles':
      return '角色管理'
    case '/admin/permissions':
      return '权限管理'
    case '/admin/profile':
      return '个人中心'
    default:
      return '控制台'
  }
})

const userName = computed(() => {
  return userStore.user?.userName || '用户'
})

const userAvatar = computed(() => {
  // 生成随机头像
  const name = userName.value
  const firstChar = name.charAt(0).toUpperCase()
  return `https://ui-avatars.com/api/?name=${firstChar}&background=random&color=fff`
})

const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
}

const handleMenuSelect = (key) => {
  activeMenu.value = key
}

const handleLogout = () => {
  userStore.logout()
  ElMessage.success('退出登录成功')
  router.push('/login')
}

onMounted(() => {
  // 初始化时获取当前用户信息
  userStore.getCurrentUser()
  // 设置当前激活的菜单
  activeMenu.value = route.path
})
</script>

<style scoped>
.admin-container {
  min-height: 100vh;
  background: #f5f7fa;
}

.admin-layout {
  min-height: 100vh;
}

.admin-sidebar {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  transition: all 0.3s ease;
  box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
}

.sidebar-header {
  padding: 20px;
  text-align: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  margin-bottom: 20px;
}

.sidebar-header h1 {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 5px 0;
}

.sidebar-header p {
  font-size: 12px;
  opacity: 0.8;
  margin: 0;
}

.sidebar-menu {
  background: transparent;
  border-right: none;
}

.sidebar-menu .el-menu-item {
  color: rgba(255, 255, 255, 0.9);
  margin: 10px 15px;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.sidebar-menu .el-menu-item:hover {
  background: rgba(255, 255, 255, 0.1);
  color: white;
}

.sidebar-menu .el-menu-item.is-active {
  background: rgba(255, 255, 255, 0.2);
  color: white;
}

.admin-main {
  flex: 1;
}

.admin-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 64px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-left h2 {
  font-size: 18px;
  font-weight: 600;
  color: white;
  margin: 0;
}

.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  cursor: pointer;
  padding: 8px 12px;
  border-radius: 20px;
  transition: all 0.3s ease;
}

.user-info:hover {
  background: #f5f7fa;
}

.user-name {
  font-size: 14px;
  font-weight: 500;
  color: white;
}

.admin-content {
  padding: 20px;
  background: #f5f7fa;
}

@media (max-width: 768px) {
  .admin-sidebar {
    width: 64px !important;
  }
  
  .sidebar-header h1,
  .sidebar-header p,
  .sidebar-menu .el-menu-item span {
    display: none;
  }
  
  .admin-content {
    padding: 10px;
  }
}
</style>