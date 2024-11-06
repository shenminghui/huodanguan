<template>
  <div class="login-container">
    <el-card class="login-card">
      <div class="login-header">
        <i class="el-icon-s-management"></i>
        <div class="title">货单管理系统</div>
      </div>
      <el-form :model="loginForm" :rules="rules" ref="loginForm">
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username" 
            prefix-icon="el-icon-user"
            placeholder="请输入用户名">
          </el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            prefix-icon="el-icon-lock"
            type="password"
            placeholder="请输入密码"
            @keyup.enter.native="handleLogin">
          </el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="handleLogin">
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginForm.validate(async valid => {
        if (valid) {
          try {
            this.loading = true
            const response = await request.post('/login', this.loginForm)
            if (response.success) {
              localStorage.setItem('token', response.data.token)
              localStorage.setItem('username', response.data.username)
              this.$message.success('登录成功')
              this.$router.push('/')
            }
          } catch (error) {
            // 不再显示错误信息，因为已经在request.js中处理了
          } finally {
            this.loading = false
          }
        }
      })
    }
  },
  created() {
    if (localStorage.getItem('token')) {
      this.$router.push('/')
    }
  }
}
</script>

<style lang="less" scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #1890ff 0%, #304156 100%);
  
  .login-card {
    width: 400px;
    padding: 30px;
    border-radius: 8px;
    box-shadow: 0 4px 16px rgba(0,0,0,0.2);
    
    .login-header {
      text-align: center;
      margin-bottom: 40px;
      
      i {
        font-size: 48px;
        color: #409EFF;
        margin-bottom: 16px;
      }
      
      .title {
        font-size: 24px;
        font-weight: bold;
        color: #303133;
      }
    }
    
    .el-form-item {
      margin-bottom: 25px;
      
      &:last-child {
        margin-bottom: 0;
      }
      
      .el-button {
        width: 100%;
        height: 40px;
        font-size: 16px;
      }
    }
    
    .el-input {
      .el-input__inner {
        height: 40px;
      }
    }
  }
}
</style> 