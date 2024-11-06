<template>
  <el-container class="layout-container">
    <el-header>
      <div class="header-left">
        <div class="collapse-btn" @click="toggleSidebar">
          <i :class="['collapse-icon', isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold']"></i>
        </div>
        <div class="header-title">
          <i class="el-icon-s-management"></i>
          <span :class="{'title-text': true, 'title-hidden': isCollapse}">货单管理系统</span>
        </div>
      </div>
      <div class="header-right">
        <span class="welcome-text">欢迎您，{{ username }}</span>
        <el-button type="text" icon="el-icon-switch-button" @click="handleLogout">退出登录</el-button>
      </div>
    </el-header>
    <el-container>
      <el-aside :width="isCollapse ? '64px' : '220px'" class="sidebar-container">
        <el-menu
          :router="true"
          :default-active="$route.path"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
          :collapse="isCollapse"
          :collapse-transition="true"
          unique-opened>
          <el-menu-item index="/home">
            <i class="el-icon-s-home"></i>
            <span slot="title">首页</span>
          </el-menu-item>
          <el-menu-item index="/delivery-orders">
            <i class="el-icon-document"></i>
            <span slot="title">货单管理</span>
          </el-menu-item>
          <el-menu-item index="/material-details">
            <i class="el-icon-box"></i>
            <span slot="title">建筑材料管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
export default {
  name: 'Layout',
  data() {
    return {
      isCollapse: false
    }
  },
  computed: {
    username() {
      return localStorage.getItem('username') || '管理员'
    }
  },
  methods: {
    toggleSidebar() {
      this.isCollapse = !this.isCollapse
    },
    handleLogout() {
      this.$confirm('确定要退出登录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('token')
        localStorage.removeItem('username')
        this.$router.push('/login')
        this.$message.success('已退出登录')
      })
    }
  }
}
</script>

<style lang="less" scoped>
.layout-container {
  height: 100vh;
  
  .el-header {
    background-color: #fff;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 1px 4px rgba(0,21,41,.08);
    padding: 0 20px;
    height: 60px;
    
    .header-left {
      display: flex;
      align-items: center;
      
      .collapse-btn {
        padding: 0 15px;
        font-size: 20px;
        color: #606266;
        cursor: pointer;
        
        .collapse-icon {
          transition: all 0.3s cubic-bezier(.645,.045,.355,1);
          transform-origin: center;
          
          &:hover {
            color: #409EFF;
            transform: scale(1.1);
          }
          
          &:active {
            transform: scale(0.95);
          }
        }
      }
      
      .header-title {
        font-size: 20px;
        font-weight: bold;
        color: #303133;
        display: flex;
        align-items: center;
        margin-left: 10px;
        
        i {
          margin-right: 8px;
          font-size: 24px;
          color: #409EFF;
        }

        .title-text {
          transition: opacity 0.3s ease-in-out;
          opacity: 1;
          
          &.title-hidden {
            opacity: 0;
          }
        }
      }
    }
    
    .header-right {
      display: flex;
      align-items: center;
      
      .welcome-text {
        margin-right: 20px;
        color: #606266;
      }
      
      .el-button {
        font-size: 14px;
        transition: all 0.3s;
        
        &:hover {
          color: #409EFF;
          transform: scale(1.05);
        }
      }
    }
  }
  
  .sidebar-container {
    background-color: #304156;
    transition: width 0.3s cubic-bezier(.645,.045,.355,1);
    box-shadow: 2px 0 6px rgba(0,21,41,.35);
    overflow: hidden;
    position: relative;
    
    &::after {
      content: '';
      position: absolute;
      top: 0;
      right: 0;
      width: 1px;
      height: 100%;
      background: rgba(255,255,255,0.1);
      transition: opacity 0.3s;
    }
    
    .el-menu {
      border-right: none;
      transition: all 0.3s cubic-bezier(.645,.045,.355,1);
      
      .el-menu-item {
        height: 56px;
        line-height: 56px;
        transition: all 0.3s cubic-bezier(.645,.045,.355,1);
        
        &:hover, &.is-active {
          background-color: #263445;
          transform: translateX(5px);
        }
        
        i {
          margin-right: 10px;
          font-size: 18px;
          transition: all 0.3s;
        }

        span {
          transition: opacity 0.3s ease-in-out;
        }
      }
    }
  }
  
  .el-main {
    background-color: #f0f2f5;
    padding: 20px;
    transition: padding 0.3s cubic-bezier(.645,.045,.355,1);
  }
}
</style> 