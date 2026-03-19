import { defineStore } from 'pinia'
import axios from 'axios'

// 创建axios实例
const apiClient = axios.create({
  baseURL: '/api',
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
apiClient.interceptors.response.use(
  (response) => {
    return response.data
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      // 未认证，清除token并跳转到登录页
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      window.location.href = '/login'
    }
    return Promise.reject(error)
  }
)

export const useUserStore = defineStore('user', {
  state: () => ({
    token: localStorage.getItem('token') || '',
    user: JSON.parse(localStorage.getItem('user') || 'null'),
    isLoggedIn: !!localStorage.getItem('token'),
    roles: [],
    permissions: []
  }),
  
  getters: {
    isAuthenticated: (state) => state.isLoggedIn,
    currentUser: (state) => state.user
  },
  
  actions: {
    // 登录
    async login(userName, password) {
      try {
        const response = await apiClient.post('/login', {
          userName,
          password
        })
        
        if (response.code === 200) {
          const token = response.extra['token:']
          const userInfo = response.data
          
          localStorage.setItem('token', token)
          localStorage.setItem('user', JSON.stringify(userInfo))
          
          this.token = token
          this.user = userInfo
          this.isLoggedIn = true
          this.roles = userInfo.roles || []
          this.permissions = userInfo.permissions || []
          
          return { success: true, message: '登录成功' }
        } else {
          return { success: false, message: response.message || '登录失败' }
        }
      } catch (error) {
        return { success: false, message: error.response?.data?.message || '登录失败' }
      }
    },
    
    // 注册
    async register(userName, password, email) {
      try {
        const response = await apiClient.post('/register', {
          userName,
          password,
          email
        })
        
        if (response.code === 200) {
          const token = response.extra['token:']
          const userInfo = response.data
          
          localStorage.setItem('token', token)
          localStorage.setItem('user', JSON.stringify(userInfo))
          
          this.token = token
          this.user = userInfo
          this.isLoggedIn = true
          this.roles = userInfo.roles || []
          this.permissions = userInfo.permissions || []
          
          return { success: true, message: '注册成功' }
        } else {
          return { success: false, message: response.message || '注册失败' }
        }
      } catch (error) {
        return { success: false, message: error.response?.data?.message || '注册失败' }
      }
    },
    
    // 登出
    logout() {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      
      this.token = ''
      this.user = null
      this.isLoggedIn = false
      this.roles = []
      this.permissions = []
    },
    
    // 获取当前用户信息
    async getCurrentUser() {
      try {
        const response = await apiClient.get('/me')
        
        if (response.code === 200) {
          this.user = response.data
          this.roles = response.data.roles || []
          this.permissions = response.data.permissions || []
          return { success: true, data: response.data }
        } else {
          return { success: false, message: response.message || '获取用户信息失败' }
        }
      } catch (error) {
        return { success: false, message: error.response?.data?.message || '获取用户信息失败' }
      }
    }
  }
})

// 导出API客户端
export { apiClient }