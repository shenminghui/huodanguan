<template>
  <div class="home">
    <div class="welcome-card">
      <i class="el-icon-s-home"></i>
      <h1>欢迎使用货单管理系统</h1>
      <p>高效便捷的货单管理解决方案</p>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home'
}
</script>

<style lang="less" scoped>
.home {
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  
  .welcome-card {
    text-align: center;
    padding: 40px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
    
    i {
      font-size: 48px;
      color: #409EFF;
      margin-bottom: 20px;
    }
    
    h1 {
      font-size: 28px;
      color: #303133;
      margin-bottom: 16px;
    }
    
    p {
      font-size: 16px;
      color: #909399;
    }
  }
}
</style> 