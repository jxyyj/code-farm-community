<template>
  <div class="login-container">
    <div class="login-card">
      <div class="login-header">
        <h2>安全框架管理平台</h2>
        <p>欢迎回来，请登录</p>
      </div>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="loginForm.userName" placeholder="admin" prefix-icon="UserFilled" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="123456" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item class="form-buttons">
          <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="register-link">
        没有账号？<a href="#" @click.prevent="goToRegister">立即注册</a>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  userName: '',
  password: ''
})

const rules = {
  userName: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const handleLogin = async () => {
  if (!loginFormRef.value) return
  
  try {
    await loginFormRef.value.validate()
    loading.value = true
    
    const result = await userStore.login(loginForm.userName, loginForm.password)
    
    if (result.success) {
      ElMessage.success(result.message)
      router.push('/admin/dashboard')
    } else {
      ElMessage.error(result.message || '登录失败')
    }
  } catch (error) {
    if (error.response && error.response.data) {
      ElMessage.error(error.response.data.message || '登录失败')
    } else {
      ElMessage.error('登录失败，请检查表单')
    }
  } finally {
    loading.value = false
  }
}

const goToRegister = () => {
  router.push('/register')
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(to bottom right, #667EEA, #eb87b1);
  padding: 20px;
}

.login-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  padding: 60px 40px;
  width: 100%;
  max-width: 400px;
  animation: fadeIn 0.5s ease-in-out;
  position: relative;
}

.login-header {
  text-align: center;
  margin-bottom: 30px;
}

.login-header h2 {
  color: #667eea;
  margin-bottom: 10px;
  font-size: 24px;
  font-weight: 600;
}

.login-header p {
  color: #666;
  font-size: 14px;
}

.login-btn {
  width: 200px;
  height: 48px;
  font-size: 16px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
}

.form-buttons {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin-top: 30px;
}

.form-buttons .el-form-item__content {
  margin-left: 0 !important;
}

.login-btn {
  width: 200px;
  height: 48px;
  font-size: 16px;
  background: linear-gradient(to bottom right, #667EEA, #eb87b1);
  border: none;
  transition: all 0.3s ease;
  margin: 0 auto;
}

.register-link {
  font-size: 14px;
  color: #666;
  position: absolute;
  bottom: 20px;
  right: 20px;
}

.register-link a {
  color: #667eea;
  text-decoration: none;
  transition: color 0.3s ease;
}

.register-link a:hover {
  color: #764ba2;
  text-decoration: underline;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

@media (max-width: 768px) {
  .login-card {
    padding: 30px;
  }
}
</style>