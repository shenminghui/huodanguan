import axios from 'axios'
import { Message } from 'element-ui'
import router from '@/router'

const service = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 请求拦截器
service.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = token
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  response => {
    const res = response.data
    if (res.success === false) {
      Message.error(res.message || '操作失败')
      return Promise.reject(new Error(res.message || '操作失败'))
    }
    return res
  },
  error => {
    if (error.response) {
      if (error.response.status === 401) {
        Message.error('登录已过期，请重新登录')
        localStorage.removeItem('token')
        router.push('/login')
      } else {
        Message.error(error.response.data.message || '请求失败')
      }
    } else {
      Message.error('网络错误，请稍后重试')
    }
    return Promise.reject(error)
  }
)

export default service 