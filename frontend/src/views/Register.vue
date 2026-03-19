<template>
  <div class="register-container">
    <div class="register-card">
      <div class="register-header">
        <h2>安全框架管理平台</h2>
        <p>创建新账号</p>
      </div>
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-width="80px">
        <el-form-item label="用户名" prop="userName">
          <el-input v-model="registerForm.userName" placeholder="请输入用户名" prefix-icon="UserFilled" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" prefix-icon="Lock" show-password />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="registerForm.email" placeholder="请输入邮箱" prefix-icon="Message" />
        </el-form-item>
        <el-form-item class="form-buttons">
          <el-button type="primary" class="register-btn" @click="handleRegister" :loading="loading">
            注册
          </el-button>
        </el-form-item>
      </el-form>
      <div class="login-link">
        已有账号？<a href="#" @click.prevent="goToLogin">立即登录</a>
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
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  userName: '',
  password: '',
  email: ''
})

const rules = {
  userName: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少 6 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ]
}

const handleRegister = async () => {
  if (!registerFormRef.value) return
  
  try {
    await registerFormRef.value.validate()
    loading.value = true
    
    const result = await userStore.register(registerForm.userName, registerForm.password, registerForm.email)
    
    if (result.success) {
      ElMessage.success(result.message)
      router.push('/admin/dashboard')
    } else {
      ElMessage.error(result.message || '注册失败')
    }
  } catch (error) {
    if (error.response && error.response.data) {
      ElMessage.error(error.response.data.message || '注册失败')
    } else {
      ElMessage.error('注册失败，请检查表单')
    }
  } finally {
    loading.value = false
  }
}

const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(to bottom right, #eb87b1, #667EEA);
  padding: 20px;
}

.register-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  padding: 60px 40px;
  width: 100%;
  max-width: 400px;
  animation: fadeIn 0.5s ease-in-out;
  position: relative;
}

.register-header {
  text-align: center;
  margin-bottom: 30px;
}

.register-header h2 {
  color: #eb87b1;
  margin-bottom: 10px;
  font-size: 24px;
  font-weight: 600;
}

.register-header p {
  color: #666;
  font-size: 14px;
}

.register-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  margin-bottom: 15px;
  background: linear-gradient(to bottom right, #eb87b1, #667EEA);
  border: none;
  transition: all 0.3s ease;
}

.register-btn:hover {
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

.login-link {
  font-size: 14px;
  color: #666;
  position: absolute;
  bottom: 20px;
  right: 20px;
}

.login-link a {
  color: #eb87b1;
  text-decoration: none;
  transition: color 0.3s ease;
}

.login-link a:hover {
  color: #667EEA;
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
  .register-card {
    padding: 30px;
  }
}
</style>