<template>
  <div class="dashboard-container">
    <el-card class="dashboard-card">
      <template #header>
        <div class="card-header">
          <h3>欢迎回来，{{ userName }}</h3>
          <p>今天是 {{ currentDate }}</p>
        </div>
      </template>
      <div class="dashboard-content">
        <div class="stats-grid">
          <el-card class="stat-card">
            <div class="stat-icon users-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <h4>用户总数</h4>
              <p class="stat-value">{{ userCount }}</p>
            </div>
          </el-card>
          <el-card class="stat-card">
            <div class="stat-icon roles-icon">
              <el-icon><Key /></el-icon>
            </div>
            <div class="stat-info">
              <h4>角色数量</h4>
              <p class="stat-value">{{ roleCount }}</p>
            </div>
          </el-card>
          <el-card class="stat-card">
            <div class="stat-icon permissions-icon">
              <el-icon><Lock /></el-icon>
            </div>
            <div class="stat-info">
              <h4>权限数量</h4>
              <p class="stat-value">{{ permissionCount }}</p>
            </div>
          </el-card>
          <el-card class="stat-card">
            <div class="stat-icon active-icon">
              <el-icon><Check /></el-icon>
            </div>
            <div class="stat-info">
              <h4>活跃用户</h4>
              <p class="stat-value">{{ activeUserCount }}</p>
            </div>
          </el-card>
        </div>
        
        <div class="recent-activities">
          <h4>最近活动</h4>
          <el-timeline>
            <el-timeline-item
              v-for="activity in recentActivities"
              :key="activity.id"
              :timestamp="activity.time"
            >
              <el-card>
                <h5>{{ activity.title }}</h5>
                <p>{{ activity.description }}</p>
              </el-card>
            </el-timeline-item>
          </el-timeline>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import { userApi, roleApi, permissionApi } from '../../services/api'
import { User, Key, Lock, Check } from '@element-plus/icons-vue'

const userStore = useUserStore()

// 统计数据
const userCount = ref(0)
const roleCount = ref(0)
const permissionCount = ref(0)
const activeUserCount = ref(0)

// 最近活动
const recentActivities = ref([
  {
    id: 1,
    title: '用户登录',
    description: '用户 admin 登录了系统',
    time: '2024-01-01 10:00:00'
  },
  {
    id: 2,
    title: '角色创建',
    description: '创建了新角色 管理员',
    time: '2024-01-01 09:30:00'
  },
  {
    id: 3,
    title: '权限更新',
    description: '更新了权限列表',
    time: '2024-01-01 08:45:00'
  }
])

// 计算属性
const userName = computed(() => {
  return userStore.user?.userName || '用户'
})

const currentDate = computed(() => {
  return new Date().toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
})

// 初始化数据
const initData = async () => {
  try {
    // 获取用户总数
    const userCountResponse = await userApi.countUsers()
    if (userCountResponse.code === 200) {
      userCount.value = userCountResponse.data || 0
    }
    
    // 获取角色数量
    const roleCountResponse = await roleApi.countRoles()
    if (roleCountResponse.code === 200) {
      roleCount.value = roleCountResponse.data || 0
    }
    
    // 获取权限数量
    const permissionCountResponse = await permissionApi.countPermissions()
    if (permissionCountResponse.code === 200) {
      permissionCount.value = permissionCountResponse.data || 0
    }
    
    // 模拟活跃用户数量
    activeUserCount.value = Math.floor(Math.random() * userCount.value) + 1
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

onMounted(() => {
  initData()
})
</script>

<style scoped>
.dashboard-container {
  width: 100%;
}

.dashboard-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h3 {
  font-size: 20px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.card-header p {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.dashboard-content {
  margin-top: 20px;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  border-radius: 12px;
  transition: all 0.3s ease;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.stat-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-bottom: 15px;
}

.users-icon {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.roles-icon {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
}

.permissions-icon {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
  color: white;
}

.active-icon {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
  color: white;
}

.stat-info h4 {
  font-size: 14px;
  color: #666;
  margin: 0 0 5px 0;
  font-weight: 500;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.recent-activities {
  margin-top: 30px;
}

.recent-activities h4 {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 20px 0;
}

.el-timeline {
  padding-left: 20px;
}

.el-timeline-item {
  margin-bottom: 20px;
}

.el-timeline-item__content {
  padding: 0;
}

.el-timeline-item__timestamp {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}

.el-card {
  border-radius: 8px;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.el-card h5 {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin: 0 0 5px 0;
}

.el-card p {
  font-size: 12px;
  color: #666;
  margin: 0;
}

@media (max-width: 768px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
  
  .card-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 5px;
  }
}
</style>