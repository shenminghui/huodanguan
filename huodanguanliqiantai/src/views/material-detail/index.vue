<template>
  <div class="material-detail">
    <!-- 搜索表单 -->
    <el-form :inline="true" :model="queryParams" class="search-form">
      <el-form-item label="开始日期">
        <el-date-picker
          v-model="queryParams.startDate"
          type="month"
          placeholder="选择开始月份"
          value-format="yyyy/MM"
          :picker-options="pickerOptions"
          :clearable="true"
          style="width: 150px">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束日期">
        <el-date-picker
          v-model="queryParams.endDate"
          type="month"
          placeholder="选择结束月份"
          value-format="yyyy/MM"
          :picker-options="pickerOptions"
          :clearable="true"
          style="width: 150px">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="收货地">
        <el-input v-model="queryParams.receivingLocation" placeholder="请输入收货地" clearable></el-input>
      </el-form-item>
      <el-form-item label="材料类型">
        <el-input v-model="queryParams.materialType" placeholder="请输入材料类型" clearable></el-input>
      </el-form-item>
      <el-form-item label="车号">
        <el-input v-model="queryParams.vehicleNumber" placeholder="请输入车号" clearable></el-input>
      </el-form-item>
      <el-form-item label="供货对账">
        <el-select v-model="queryParams.supplyReconciliationStatus" placeholder="请选择" clearable>
          <el-option label="已对账" value="已对账"></el-option>
          <el-option label="未对账" value="未对账"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="供货收款">
        <el-select v-model="queryParams.supplyPaymentStatus" placeholder="请选择" clearable>
          <el-option label="已收款" value="已收款"></el-option>
          <el-option label="未收款" value="未收款"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="出厂对账">
        <el-select v-model="queryParams.factoryReconciliationStatus" placeholder="请选择" clearable>
          <el-option label="已对账" value="已对账"></el-option>
          <el-option label="未对账" value="未对账"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="出厂付款">
        <el-select v-model="queryParams.factoryPaymentStatus" placeholder="请选择" clearable>
          <el-option label="已付款" value="已付款"></el-option>
          <el-option label="未付款" value="未付款"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="handleQuery">查询</el-button>
        <el-button @click="resetQuery">重置</el-button>
        <el-button type="success" @click="handleAdd">新增材料</el-button>
        <el-button type="warning" @click="handleExport">导出Excel</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table :data="detailList" border style="width: 100%" v-loading="loading" size="mini">
      <!-- 基本信息列 -->
      <el-table-column label="基本信息" width="300">
        <template slot-scope="scope">
          <div class="info-group">
            <div class="info-row">
              <span class="label">日期：</span>
              <span class="value">{{ scope.row.detailDate }}</span>
            </div>
            <div class="info-row">
              <span class="label">收货地：</span>
              <span class="value">{{ scope.row.receivingLocation }}</span>
            </div>
            <div class="info-row">
              <span class="label">材料类型：</span>
              <span class="value">{{ scope.row.materialType }}</span>
            </div>
            <div class="info-row">
              <span class="label">车次：</span>
              <span class="value">{{ scope.row.trips }}</span>
            </div>
            <div class="info-row">
              <span class="label">车号：</span>
              <span class="value">{{ scope.row.vehicleNumber }}</span>
            </div>
          </div>
        </template>
      </el-table-column>

      <!-- 供货信息列 -->
      <el-table-column label="供货信息" width="250">
        <template slot-scope="scope">
          <div class="info-group">
            <div class="info-row">
              <span class="label">供货吨数：</span>
              <span class="value">{{ scope.row.supplyTonnage }}</span>
            </div>
            <div class="info-row">
              <span class="label">供货单价：</span>
              <span class="value">{{ scope.row.supplyUnitPrice }}</span>
            </div>
            <div class="info-row">
              <span class="label">供货金额：</span>
              <span class="value">{{ scope.row.supplyAmount }}</span>
            </div>
          </div>
        </template>
      </el-table-column>

      <!-- 出厂信息列 -->
      <el-table-column label="出厂信息" width="250">
        <template slot-scope="scope">
          <div class="info-group">
            <div class="info-row">
              <span class="label">出厂吨数：</span>
              <span class="value">{{ scope.row.factoryTonnage }}</span>
            </div>
            <div class="info-row">
              <span class="label">出厂单价：</span>
              <span class="value">{{ scope.row.factoryUnitPrice }}</span>
            </div>
            <div class="info-row">
              <span class="label">出厂金额：</span>
              <span class="value">{{ scope.row.factoryAmount }}</span>
            </div>
          </div>
        </template>
      </el-table-column>

      <!-- 状态列 -->
      <el-table-column label="状态" width="180">
        <template slot-scope="scope">
          <div class="status-buttons">
            <div class="status-row">
              <el-button
                size="mini"
                :type="getButtonType(scope.row.supplyReconciliationStatus)"
                class="status-btn"
                @click="handleStatusChange(scope.row.id, 'supplyReconciliation', scope.row.supplyReconciliationStatus)">
                {{ scope.row.supplyReconciliationStatus }}
              </el-button>
              <el-button
                size="mini"
                :type="getButtonType(scope.row.supplyPaymentStatus)"
                class="status-btn"
                @click="handleStatusChange(scope.row.id, 'supplyPayment', scope.row.supplyPaymentStatus)">
                {{ scope.row.supplyPaymentStatus }}
              </el-button>
            </div>
            <div class="status-row">
              <el-button
                size="mini"
                :type="getButtonType(scope.row.factoryReconciliationStatus)"
                class="status-btn"
                @click="handleStatusChange(scope.row.id, 'factoryReconciliation', scope.row.factoryReconciliationStatus)">
                {{ scope.row.factoryReconciliationStatus }}
              </el-button>
              <el-button
                size="mini"
                :type="getButtonType(scope.row.factoryPaymentStatus)"
                class="status-btn"
                @click="handleStatusChange(scope.row.id, 'factoryPayment', scope.row.factoryPaymentStatus)">
                {{ scope.row.factoryPaymentStatus }}
              </el-button>
            </div>
          </div>
        </template>
      </el-table-column>

      <!-- 单据图片列 -->
      <el-table-column label="单据图片" width="200">
        <template slot-scope="scope">
          <div class="image-group">
            <!-- 供货单图片 -->
            <div class="image-item">
              <span class="image-label">供货单</span>
              <div v-if="scope.row.supplyImage" class="image-preview" @click="handlePreviewImage(scope.row.supplyImage)">
                <el-image 
                  class="table-image"
                  :src="scope.row.supplyImage"
                  fit="cover"
                  :preview-src-list="[scope.row.supplyImage]">
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </div>
              <div v-else class="no-image">
                <i class="el-icon-picture-outline"></i>
                <span>暂无图片</span>
              </div>
            </div>

            <!-- 出厂单图片 -->
            <div class="image-item">
              <span class="image-label">出厂单</span>
              <div v-if="scope.row.factoryImage" class="image-preview" @click="handlePreviewImage(scope.row.factoryImage)">
                <el-image 
                  class="table-image"
                  :src="scope.row.factoryImage"
                  fit="cover"
                  :preview-src-list="[scope.row.factoryImage]">
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </div>
              <div v-else class="no-image">
                <i class="el-icon-picture-outline"></i>
                <span>暂无图片</span>
              </div>
            </div>
          </div>
        </template>
      </el-table-column>

      <!-- 操作列 -->
      <el-table-column label="操作" width="120" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="mini" class="delete-btn" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="queryParams.pageNum"
      :page-sizes="[5, 10, 20, 30, 50, 100]"
      :page-size="queryParams.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total">
    </el-pagination>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="80%" @close="resetForm">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px" size="small">
        <!-- 基本信息 -->
        <div class="form-section">
          <div class="section-title">基本信息</div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="日期" prop="detailDate">
                <el-date-picker
                  v-model="form.detailDate"
                  type="date"
                  placeholder="选择日期"
                  value-format="yyyy/MM/dd"
                  style="width: 100%">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="收货地" prop="receivingLocation">
                <el-input v-model="form.receivingLocation" placeholder="请输入收货地"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="材料类型" prop="materialType">
                <el-input v-model="form.materialType" placeholder="请输入材料��型"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="车次" prop="trips">
                <el-input-number v-model="form.trips" :min="1" placeholder="请输入车次"></el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="车号" prop="vehicleNumber">
                <el-input v-model="form.vehicleNumber" placeholder="请输入车号"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 供货信息 -->
        <div class="form-section">
          <div class="section-title">供货信息</div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="供货吨数" prop="supplyTonnage">
                <el-input-number 
                  v-model="form.supplyTonnage" 
                  :precision="2" 
                  :step="0.1" 
                  :min="0"
                  style="width: 100%"
                  placeholder="请输入供货吨数">
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="供货单价" prop="supplyUnitPrice">
                <el-input-number 
                  v-model="form.supplyUnitPrice" 
                  :precision="2" 
                  :step="0.1" 
                  :min="0"
                  style="width: 100%"
                  placeholder="请输入供货单价">
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="供货金额" prop="supplyAmount">
                <el-input v-model="form.supplyAmount" disabled placeholder="自动计算"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="供货单图片" prop="supplyImage">
                <el-upload
                  class="upload-demo"
                  action="/api/material-details/upload"
                  :headers="uploadHeaders"
                  :on-success="handleSupplyImageSuccess"
                  :on-error="handleUploadError"
                  :before-upload="beforeImageUpload"
                  :file-list="supplyImageList"
                  :limit="1"
                  list-type="picture">
                  <el-button size="small" type="primary">点击上传</el-button>
                  <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 出厂信息 -->
        <div class="form-section">
          <div class="section-title">出厂信息</div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="出厂吨数" prop="factoryTonnage">
                <el-input-number 
                  v-model="form.factoryTonnage" 
                  :precision="2" 
                  :step="0.1" 
                  :min="0"
                  style="width: 100%"
                  placeholder="请输入出厂吨数">
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="出厂单价" prop="factoryUnitPrice">
                <el-input-number 
                  v-model="form.factoryUnitPrice" 
                  :precision="2" 
                  :step="0.1" 
                  :min="0"
                  style="width: 100%"
                  placeholder="请输入出厂单价">
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="出厂金额" prop="factoryAmount">
                <el-input v-model="form.factoryAmount" disabled placeholder="自动计算"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="出厂单图片" prop="factoryImage">
                <el-upload
                  class="upload-demo"
                  action="/api/material-details/upload"
                  :headers="uploadHeaders"
                  :on-success="handleFactoryImageSuccess"
                  :on-error="handleUploadError"
                  :before-upload="beforeImageUpload"
                  :file-list="factoryImageList"
                  :limit="1"
                  list-type="picture">
                  <el-button size="small" type="primary">点击上传</el-button>
                  <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过2MB</div>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 备注信息 -->
        <div class="form-section">
          <div class="section-title">备注信息</div>
          <el-form-item label="备注" prop="remarks">
            <el-input type="textarea" v-model="form.remarks" :rows="3" placeholder="请输入备注信息"></el-input>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitForm" :loading="submitLoading">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import request from '@/utils/request'

export default {
  name: 'MaterialDetail',
  data() {
    return {
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        startDate: '',
        endDate: '',
        receivingLocation: '',
        materialType: '',
        vehicleNumber: '',
        supplyReconciliationStatus: '',
        supplyPaymentStatus: '',
        factoryReconciliationStatus: '',
        factoryPaymentStatus: ''
      },
      // 数据列表
      detailList: [],
      total: 0,
      loading: false,
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      // 表单数据
      form: {
        detailDate: '',
        receivingLocation: '',
        materialType: '',
        trips: 1,
        vehicleNumber: '',
        supplyTonnage: 0,
        supplyUnitPrice: 0,
        supplyAmount: 0,
        supplyReconciliationStatus: '未对账',
        supplyPaymentStatus: '未收款',
        supplyImage: '',
        factoryTonnage: 0,
        factoryUnitPrice: 0,
        factoryAmount: 0,
        factoryReconciliationStatus: '未对账',
        factoryPaymentStatus: '未付款',
        factoryImage: '',
        remarks: ''
      },
      // 表单校验规则
      rules: {
        detailDate: [
          { required: true, message: '请选择日期', trigger: 'change' }
        ],
        receivingLocation: [
          { required: true, message: '请输入收货地', trigger: 'blur' }
        ],
        materialType: [
          { required: true, message: '请输入材料类型', trigger: 'blur' }
        ],
        trips: [
          { required: true, message: '请输入车次', trigger: 'blur' }
        ],
        vehicleNumber: [
          { required: true, message: '请输入车号', trigger: 'blur' }
        ],
        supplyTonnage: [
          { required: true, message: '请输入供货吨数', trigger: 'blur' }
        ],
        supplyUnitPrice: [
          { required: true, message: '请输入供货单价', trigger: 'blur' }
        ],
        factoryTonnage: [
          { required: true, message: '请输入出厂吨数', trigger: 'blur' }
        ],
        factoryUnitPrice: [
          { required: true, message: '请输入出厂单价', trigger: 'blur' }
        ]
      },
      // 其他数据...
      pickerOptions: {
        // 设置年份范围，这里设置为前后10年
        disabledDate(time) {
          const start = new Date();
          start.setFullYear(start.getFullYear() - 10);
          const end = new Date();
          end.setFullYear(end.getFullYear() + 10);
          return time.getTime() < start.getTime() || time.getTime() > end.getTime();
        }
      }
    }
  },
  methods: {
    // 获取列表数据
    getList() {
      this.loading = true
      request.post('/material-details/page', this.queryParams)
        .then(response => {
          if (response.success) {
            this.detailList = response.data.records
            this.total = response.data.total
          }
        })
        .catch(error => {
          console.error('获取材料明细列表失败：', error)
          this.$message.error('获取材料明细列表失败')
        })
        .finally(() => {
          this.loading = false
        })
    },

    // 获取按钮类型
    getButtonType(status) {
      return status.startsWith('已') ? 'success' : 'danger'
    },

    // 处理状态变更
    async handleStatusChange(id, field, currentStatus) {
      try {
        const newStatus = currentStatus.startsWith('未') ? 
          currentStatus.replace('未', '已') : 
          currentStatus.replace('已', '未')
        
        await request.put(`/material-details/${id}/status`, null, {
          params: { field, status: newStatus }
        })
        
        // 立即更新本地数据状态
        const index = this.detailList.findIndex(item => item.id === id)
        if (index !== -1) {
          const detail = this.detailList[index]
          switch (field) {
            case 'supplyReconciliation':
              detail.supplyReconciliationStatus = newStatus
              break
            case 'supplyPayment':
              detail.supplyPaymentStatus = newStatus
              break
            case 'factoryReconciliation':
              detail.factoryReconciliationStatus = newStatus
              break
            case 'factoryPayment':
              detail.factoryPaymentStatus = newStatus
              break
          }
          this.$set(this.detailList, index, { ...detail })
        }
        
        this.$message.success('状态更新成功')
      } catch (error) {
        this.$message.error('状态更新失败')
      }
    },

    // 处理供货单图片上传成功
    handleSupplyImageSuccess(response) {
      if (response.success) {
        const imageUrl = response.data;
        this.form.supplyImage = imageUrl;
        this.supplyImageList = [{
          name: '供货单图片',
          url: imageUrl
        }];
        this.$message.success('上传成功');
      } else {
        this.$message.error(response.message || '上传失败');
      }
    },

    // 处理出厂单图片上传成功
    handleFactoryImageSuccess(response) {
      if (response.success) {
        const imageUrl = response.data;
        this.form.factoryImage = imageUrl;
        this.factoryImageList = [{
          name: '出厂单图片',
          url: imageUrl
        }];
        this.$message.success('上传成功');
      } else {
        this.$message.error(response.message || '上传失败');
      }
    },

    // 处理上传错误
    handleUploadError(err) {
      console.error('上传失败：', err);
      this.$message.error('上传失败，请稍后重试');
    },

    // 上传前校验
    beforeImageUpload(file) {
      const isImage = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error('只能上传JPG/PNG格式的图片!');
        return false;
      }
      if (!isLt2M) {
        this.$message.error('图片大小不能超过2MB!');
        return false;
      }
      return true;
    },

    // 处理查询
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    // 处理重置
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        startDate: '',
        endDate: '',
        receivingLocation: '',
        materialType: '',
        vehicleNumber: '',
        supplyReconciliationStatus: '',
        supplyPaymentStatus: '',
        factoryReconciliationStatus: '',
        factoryPaymentStatus: ''
      }
      this.getList()
    },

    // 处理新增
    handleAdd() {
      this.dialogTitle = '新增建筑材料'
      this.dialogVisible = true
      this.resetForm()
    },

    // 处理编辑
    handleEdit(row) {
      this.dialogTitle = '编辑建筑材料'
      this.dialogVisible = true
      this.form = { ...row }
      // 设置图片列表
      if (row.supplyImage) {
        this.supplyImageList = [{
          name: '供货单图片',
          url: row.supplyImage
        }]
      } else {
        this.supplyImageList = []
      }
      if (row.factoryImage) {
        this.factoryImageList = [{
          name: '出厂单图片',
          url: row.factoryImage
        }]
      } else {
        this.factoryImageList = []
      }
    },

    // 处理删除
    handleDelete(row) {
      this.$confirm('确认删除该记录?', '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await request.delete(`/material-details/${row.id}`)
          this.$message.success('删除成功')
          this.getList()
        } catch (error) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },

    // 处理导出
    handleExport() {
      this.$confirm('确认导出当前查询结果?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.exportData()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消导出'
        })
      })
    },

    // 执行导出
    async exportData() {
      try {
        this.$message({
          type: 'info',
          message: '正在导出，请稍候...'
        })
        
        const response = await request.post('/material-details/export', this.queryParams, {
          responseType: 'blob'
        })
        
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = '建筑材料明细.xlsx'
        link.click()
        window.URL.revokeObjectURL(link.href)
        
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败：', error)
        this.$message.error('导出失败，请稍后重试')
      }
    },

    // 处理分页大小变化
    handleSizeChange(val) {
      this.queryParams.pageSize = val
      this.getList()
    },

    // 处理页码变化
    handleCurrentChange(val) {
      this.queryParams.pageNum = val
      this.getList()
    },

    // 重置表单
    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
      this.form = {
        detailDate: '',
        receivingLocation: '',
        materialType: '',
        trips: 1,
        vehicleNumber: '',
        supplyTonnage: 0,
        supplyUnitPrice: 0,
        supplyAmount: 0,
        supplyReconciliationStatus: '未对账',
        supplyPaymentStatus: '未收款',
        supplyImage: '',
        factoryTonnage: 0,
        factoryUnitPrice: 0,
        factoryAmount: 0,
        factoryReconciliationStatus: '未对账',
        factoryPaymentStatus: '未付款',
        factoryImage: '',
        remarks: ''
      }
      this.supplyImageList = []
      this.factoryImageList = []
    },

    // 提交表单
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.submitLoading = true
          const method = this.form.id ? 'put' : 'post'
          request[method]('/material-details', this.form)
            .then(() => {
              this.$message.success('保存成功')
              this.dialogVisible = false
              this.getList()
            })
            .catch(error => {
              console.error('保存失败：', error)
              this.$message.error(error.message || '保存失败')
            })
            .finally(() => {
              this.submitLoading = false
            })
        }
      })
    }
  },
  watch: {
    // 监听供货吨数和单价，自动计算金额
    'form.supplyTonnage'() {
      this.form.supplyAmount = (
        (parseFloat(this.form.supplyTonnage) || 0) * 
        (parseFloat(this.form.supplyUnitPrice) || 0)
      ).toFixed(2)
    },
    'form.supplyUnitPrice'() {
      this.form.supplyAmount = (
        (parseFloat(this.form.supplyTonnage) || 0) * 
        (parseFloat(this.form.supplyUnitPrice) || 0)
      ).toFixed(2)
    },
    // 监听出厂吨数和单价，自动计算金额
    'form.factoryTonnage'() {
      this.form.factoryAmount = (
        (parseFloat(this.form.factoryTonnage) || 0) * 
        (parseFloat(this.form.factoryUnitPrice) || 0)
      ).toFixed(2)
    },
    'form.factoryUnitPrice'() {
      this.form.factoryAmount = (
        (parseFloat(this.form.factoryTonnage) || 0) * 
        (parseFloat(this.form.factoryUnitPrice) || 0)
      ).toFixed(2)
    }
  },
  created() {
    this.getList()
  }
}
</script>

<style lang="less" scoped>
.material-detail {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);

  // 复用之前的样式...
}
</style> 