<template>
  <div class="delivery-order">
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
      <el-form-item label="工程名称">
        <el-input v-model="queryParams.projectName" placeholder="请输入工程名称" clearable></el-input>
      </el-form-item>
      <el-form-item label="收货单位">
        <el-input v-model="queryParams.receivingUnit" placeholder="请输入收货单位" clearable></el-input>
      </el-form-item>
      <el-form-item label="车号">
        <el-input v-model="queryParams.vehicleNumber" placeholder="请输入车号" clearable></el-input>
      </el-form-item>
      <el-form-item label="司机姓名">
        <el-input v-model="queryParams.driverName" placeholder="请输入司机姓名" clearable></el-input>
      </el-form-item>
      <el-form-item label="商混站">
        <el-input v-model="queryParams.mixingStation" placeholder="请输入商混站" clearable></el-input>
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
        <el-button type="success" @click="handleAdd">新增货单</el-button>
        <el-button type="warning" @click="handleExport">导出Excel</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table :data="orderList" border style="width: 100%" v-loading="loading" size="mini">
      <!-- 基本信息列 -->
      <el-table-column label="基本信息" width="300">
        <template slot-scope="scope">
          <div class="info-group">
            <div class="info-row">
              <span class="label">日期：</span>
              <span class="value">{{ scope.row.orderDate }}</span>
            </div>
            <div class="info-row">
              <span class="label">工程：</span>
              <span class="value">{{ scope.row.projectName }}</span>
            </div>
            <div class="info-row">
              <span class="label">浇筑部位：</span>
              <span class="value">{{ scope.row.pouringPosition }}</span>
            </div>
            <div class="info-row">
              <span class="label">收货单位：</span>
              <span class="value">{{ scope.row.receivingUnit }}</span>
            </div>
            <div class="info-row">
              <span class="label">车号/司机：</span>
              <span class="value">{{ scope.row.vehicleNumber }} / {{ scope.row.driverName }}</span>
            </div>
            <div class="info-row">
              <span class="label">商混站：</span>
              <span class="value">{{ scope.row.mixingStation }}</span>
            </div>
          </div>
        </template>
      </el-table-column>

      <!-- 供货信息列 -->
      <el-table-column label="供货信息" width="250">
        <template slot-scope="scope">
          <div class="info-group">
            <div class="info-row">
              <span class="label">标号/方量：</span>
              <span class="value">{{ scope.row.supplyGrade }} / {{ scope.row.supplyVolume }}</span>
            </div>
            <div class="info-row">
              <span class="label">单价/金额：</span>
              <span class="value">{{ scope.row.supplyUnitPrice }} / {{ scope.row.supplyAmount }}</span>
            </div>
            <div class="info-row">
              <span class="label">超时费：</span>
              <span class="value">{{ scope.row.supplyOvertimeCost }}</span>
            </div>
            <div class="info-row">
              <span class="label">补运费：</span>
              <span class="value">{{ scope.row.extraFreightCost }}</span>
            </div>
          </div>
        </template>
      </el-table-column>

      <!-- 出厂信息列 -->
      <el-table-column label="出厂信息" width="250">
        <template slot-scope="scope">
          <div class="info-group">
            <div class="info-row">
              <span class="label">标号/方量：</span>
              <span class="value">{{ scope.row.factoryGrade }} / {{ scope.row.factoryVolume }}</span>
            </div>
            <div class="info-row">
              <span class="label">单价/金额：</span>
              <span class="value">{{ scope.row.factoryUnitPrice }} / {{ scope.row.factoryAmount }}</span>
            </div>
            <div class="info-row">
              <span class="label">超时费：</span>
              <span class="value">{{ scope.row.factoryOvertimeCost }}</span>
            </div>
          </div>
        </template>
      </el-table-column>

      <!-- 费用信息列 -->
      <el-table-column label="费用信息" width="200">
        <template slot-scope="scope">
          <div class="info-group">
            <div class="info-row">
              <span class="label">运费：</span>
              <span class="value">{{ scope.row.freightCost }}</span>
            </div>
            <div class="info-row">
              <span class="label">税费：</span>
              <span class="value">{{ scope.row.taxCost }}</span>
            </div>
            <div class="info-row">
              <span class="label">方量差异：</span>
              <span class="value" :class="getDifferenceClass(scope.row.volumeDifference)">
                {{ scope.row.volumeDifference }}
              </span>
            </div>
            <div class="info-row">
              <span class="label">利润：</span>
              <span class="value" :class="getDifferenceClass(scope.row.profit)">
                {{ scope.row.profit }}
              </span>
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

      <!-- 操作列 -->
      <el-table-column label="操作" width="120" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="mini" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="mini" class="delete-btn" @click="handleDelete(scope.row)">删除</el-button>
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
    </el-table>

    <!-- 添加汇总信息区域 -->
    <div class="summary-section">
      <div class="summary-title">汇总信息</div>
      <div class="summary-content">
        <div class="summary-item">
          <span class="label">总条数：</span>
          <span class="value">{{ total }} 条</span>
        </div>
        <div class="summary-item">
          <span class="label">总供货方量：</span>
          <span class="value">{{ formatNumber(summaryData.totalSupplyVolume) }} 方</span>
        </div>
        <div class="summary-item">
          <span class="label">总供货金额：</span>
          <span class="value">{{ formatNumber(summaryData.totalSupplyAmount) }} 元</span>
        </div>
        <div class="summary-item">
          <span class="label">总出厂方量：</span>
          <span class="value">{{ formatNumber(summaryData.totalFactoryVolume) }} 方</span>
        </div>
        <div class="summary-item">
          <span class="label">总出厂金额：</span>
          <span class="value">{{ formatNumber(summaryData.totalFactoryAmount) }} 元</span>
        </div>
        <div class="summary-item">
          <span class="label">总运费：</span>
          <span class="value">{{ formatNumber(summaryData.totalFreightCost) }} 元</span>
        </div>
        <div class="summary-item">
          <span class="label">总税费：</span>
          <span class="value">{{ formatNumber(summaryData.totalTaxCost) }} 元</span>
        </div>
        <div class="summary-item">
          <span class="label">总方量差异：</span>
          <span class="value" :class="getDifferenceClass(summaryData.totalVolumeDifference)">
            {{ formatNumber(summaryData.totalVolumeDifference) }} 方
          </span>
        </div>
        <div class="summary-item">
          <span class="label">总利润：</span>
          <span class="value" :class="getDifferenceClass(summaryData.totalProfit)">
            {{ formatNumber(summaryData.totalProfit) }} 元
          </span>
        </div>
      </div>
    </div>

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

    <!-- 新增/编辑话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="80%" @close="resetForm" class="delivery-dialog">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px" size="small">
        <!-- 基本信息 -->
        <div class="form-section">
          <div class="section-title">基本信息</div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="日期" prop="orderDate">
                <el-input v-model="form.orderDate" placeholder="请输入日期"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="工程名称" prop="projectName">
                <el-input v-model="form.projectName" placeholder="请输入工程名称"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="浇筑位" prop="pouringPosition">
                <el-input v-model="form.pouringPosition" placeholder="请输入浇筑部位"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="商混站名字" prop="mixingStation">
                <el-input v-model="form.mixingStation" placeholder="请输入商混站"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="收货单位" prop="receivingUnit">
                <el-input v-model="form.receivingUnit" placeholder="请输入收货单位"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="车号" prop="vehicleNumber">
                <el-input v-model="form.vehicleNumber" placeholder="请输入车号"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="司机姓名" prop="driverName">
                <el-input v-model="form.driverName" placeholder="请输入司机姓名"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 供货信息 -->
        <div class="form-section">
          <div class="section-title">供货信息</div>
          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="供货标号" prop="supplyGrade">
                <el-input v-model="form.supplyGrade" placeholder="请输入供货标号"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="供方量" prop="supplyVolume">
                <el-input v-model="form.supplyVolume" placeholder="输入供货方量"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="供货车次" prop="supplyTrips">
                <el-input v-model="form.supplyTrips" placeholder="请输入供货车次"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="供货单价" prop="supplyUnitPrice">
                <el-input v-model="form.supplyUnitPrice" placeholder="请输入供货单价"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="供货金额" prop="supplyAmount">
                <el-input v-model="form.supplyAmount" placeholder="请输入供货金额" :disabled="true"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="供货超时费" prop="supplyOvertimeCost">
                <el-input v-model="form.supplyOvertimeCost" placeholder="请输入供货超时费"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="补运费" prop="extraFreightCost">
                <el-input v-model="form.extraFreightCost" placeholder="请输入补运费"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="供货单图片" prop="supplyImage">
                <el-upload
                  class="upload-demo"
                  action="/api/delivery-orders/upload"
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
            <el-col :span="6">
              <el-form-item label="出厂标号" prop="factoryGrade">
                <el-input v-model="form.factoryGrade" placeholder="请输入出厂标号"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="出厂方量" prop="factoryVolume">
                <el-input v-model="form.factoryVolume" placeholder="请输入出厂方量"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="出厂单价" prop="factoryUnitPrice">
                <el-input v-model="form.factoryUnitPrice" placeholder="请输入出厂单价"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="出厂金额" prop="factoryAmount">
                <el-input v-model="form.factoryAmount" placeholder="请输入出厂金额" :disabled="true"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="6">
              <el-form-item label="出厂超时费" prop="factoryOvertimeCost">
                <el-input v-model="form.factoryOvertimeCost" placeholder="请输入出厂超时费"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="24">
              <el-form-item label="出厂单图片" prop="factoryImage">
                <el-upload
                  class="upload-demo"
                  action="/api/delivery-orders/upload"
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

        <!-- 运费和税费 -->
        <div class="form-section">
          <div class="section-title">运费税费</div>
          <el-row :gutter="20">
            <el-col :span="8">
              <el-form-item label="运费" prop="freightCost">
                <el-input v-model="form.freightCost" placeholder="请输入运费"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="税费" prop="taxCost">
                <el-input v-model="form.taxCost" placeholder="请输入税费"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 备注信息 -->
        <div class="form-section">
          <div class="section-title">备注信息</div>
          <el-form-item label="注" prop="remarks">
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
  name: 'DeliveryOrder',
  data() {
    return {
      // 添加���期范围数据
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        startDate: '',
        endDate: '',
        projectName: '',
        receivingUnit: '',
        vehicleNumber: '',
        driverName: '',
        mixingStation: '',
        supplyReconciliationStatus: '',
        supplyPaymentStatus: '',
        factoryReconciliationStatus: '',
        factoryPaymentStatus: ''
      },
      // 据列表
      orderList: [],
      total: 0,
      loading: false,
      // 对话框
      dialogVisible: false,
      dialogTitle: '',
      form: {
        orderDate: '',
        projectName: '',
        pouringPosition: '',
        receivingUnit: '',
        vehicleNumber: '',
        driverName: '',
        mixingStation: '',
        supplyGrade: '',
        supplyVolume: '',
        supplyTrips: '',
        supplyUnitPrice: '',
        supplyAmount: '',
        freightCost: '',
        overtimeCost: '',
        supplyImage: '',
        factoryGrade: '',
        factoryVolume: '',
        factoryUnitPrice: '',
        factoryAmount: '',
        factoryImage: '',
        remarks: '',
        supplyReconciliationStatus: '未对账',
        supplyPaymentStatus: '未收款',
        factoryReconciliationStatus: '未对账',
        factoryPaymentStatus: '未付款'
      },
      rules: {
        orderDate: [
          { required: true, message: '请输入日期', trigger: 'blur' }
        ],
        projectName: [
          { required: true, message: '请输入工程名称', trigger: 'blur' }
        ],
        pouringPosition: [
          { required: true, message: '请输入浇筑部位', trigger: 'blur' }
        ],
        receivingUnit: [
          { required: true, message: '请输入收货单', trigger: 'blur' }
        ],
        vehicleNumber: [
          { required: true, message: '请输入车号', trigger: 'blur' }
        ],
        driverName: [
          { required: true, message: '请输入司机姓名', trigger: 'blur' }
        ],
        mixingStation: [
          { required: true, message: '请输入商混站名字', trigger: 'blur' }
        ],
        supplyGrade: [
          { required: true, message: '请输入供货标号', trigger: 'blur' }
        ],
        supplyVolume: [
          { required: true, message: '请输入供货方量', trigger: 'blur' }
        ],
        supplyTrips: [
          { required: true, message: '请输入供货车次', trigger: 'blur' }
        ],
        supplyUnitPrice: [
          { required: true, message: '请输入供货单价', trigger: 'blur' }
        ],
        supplyAmount: [
          { required: true, message: '请输入供货金额', trigger: 'blur' }
        ],
        supplyOvertimeCost: [
          { required: true, message: '请输入供货超时费', trigger: 'blur' }
        ],
        extraFreightCost: [
          { required: true, message: '请输入补运费', trigger: 'blur' }
        ],
        factoryGrade: [
          { required: true, message: '请输入出厂标号', trigger: 'blur' }
        ],
        factoryVolume: [
          { required: true, message: '请入出厂方量', trigger: 'blur' }
        ],
        factoryUnitPrice: [
          { required: true, message: '请输入出厂单价', trigger: 'blur' }
        ],
        factoryAmount: [
          { required: true, message: '请输入出厂金额', trigger: 'blur' }
        ],
        factoryOvertimeCost: [
          { required: true, message: '请输入出厂超时费', trigger: 'blur' }
        ],
        freightCost: [
          { required: true, message: '请输入运费', trigger: 'blur' }
        ],
        taxCost: [
          { required: true, message: '请输入税费', trigger: 'blur' }
        ]
      },
      supplyImageList: [],
      factoryImageList: [],
      submitLoading: false,
      summaryData: {
        totalSupplyVolume: 0,
        totalSupplyAmount: 0,
        totalFactoryVolume: 0,
        totalFactoryAmount: 0,
        totalFreightCost: 0,
        totalTaxCost: 0,
        totalVolumeDifference: 0,
        totalProfit: 0
      },
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
  created() {
    this.getList()
  },
  methods: {
    // 获取列表数据
    getList() {
      this.loading = true
      const params = {
        ...this.queryParams,
        pageNum: this.queryParams.pageNum,
        pageSize: this.queryParams.pageSize
      }
      
      // 处理日期范围
      if (this.dateRange && this.dateRange.length === 2) {
        params.startDate = this.dateRange[0]
        params.endDate = this.dateRange[1]
      }

      request.post('/delivery-orders/page', params)
        .then(response => {
          if (response.success) {
            this.orderList = response.data.records
            this.total = response.data.total
            // 计算汇总据
            this.calculateSummary()
          }
        })
        .catch(error => {
          console.error('获取货单列表失败：', error)
          this.$message.error('获取货单列表失败')
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
        
        await request.put(`/delivery-orders/${id}/status`, null, {
          params: { field, status: newStatus }
        })
        
        // 立即更新本地数据状态
        const index = this.orderList.findIndex(item => item.id === id)
        if (index !== -1) {
          const order = this.orderList[index]
          switch (field) {
            case 'supplyReconciliation':
              order.supplyReconciliationStatus = newStatus
              break
            case 'supplyPayment':
              order.supplyPaymentStatus = newStatus
              break
            case 'factoryReconciliation':
              order.factoryReconciliationStatus = newStatus
              break
            case 'factoryPayment':
              order.factoryPaymentStatus = newStatus
              break
          }
          // 强制更新视图
          this.$set(this.orderList, index, { ...order })
        }
        
        this.$message.success('状态更新成功')
      } catch (error) {
        this.$message.error('状态更新失败')
      }
    },

    // 处理新增
    handleAdd() {
      this.dialogTitle = '新增货单'
      this.dialogVisible = true
      this.form = {
        orderDate: '',
        supplyReconciliationStatus: '未对账',
        supplyPaymentStatus: '未收款',
        factoryReconciliationStatus: '未对账',
        factoryPaymentStatus: '未付款'
      }
    },

    // 处理编辑
    handleEdit(row) {
      this.dialogTitle = '编辑货单'
      this.dialogVisible = true
      this.form = { ...row }
      // 设置图片列表
      if (row.supplyImage) {
        this.supplyImageList = [{
          name: '供货单图片',
          url: row.supplyImage
        }];
      } else {
        this.supplyImageList = [];
      }
      if (row.factoryImage) {
        this.factoryImageList = [{
          name: '出厂单图片',
          url: row.factoryImage
        }];
      } else {
        this.factoryImageList = [];
      }
    },

    // 处理删除
    handleDelete(row) {
      this.$confirm('确认删除该货单?', '提示', {
        type: 'warning'
      }).then(async () => {
        try {
          await request.delete(`/delivery-orders/${row.id}`)
          this.$message.success('删除成功')
          this.getList()
        } catch (error) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },

    // 提交表单
    submitForm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          try {
            this.submitLoading = true
            // 构建提交的数据
            const formData = {
              ...this.form,
              // 转换数字字段
              supplyVolume: this.form.supplyVolume ? Number(this.form.supplyVolume) : 0,
              supplyUnitPrice: this.form.supplyUnitPrice ? Number(this.form.supplyUnitPrice) : 0,
              supplyAmount: this.form.supplyAmount ? Number(this.form.supplyAmount) : 0,
              supplyOvertimeCost: this.form.supplyOvertimeCost ? Number(this.form.supplyOvertimeCost) : 0,
              extraFreightCost: this.form.extraFreightCost ? Number(this.form.extraFreightCost) : 0,
              freightCost: this.form.freightCost ? Number(this.form.freightCost) : 0,
              factoryVolume: this.form.factoryVolume ? Number(this.form.factoryVolume) : 0,
              factoryUnitPrice: this.form.factoryUnitPrice ? Number(this.form.factoryUnitPrice) : 0,
              factoryAmount: this.form.factoryAmount ? Number(this.form.factoryAmount) : 0,
              factoryOvertimeCost: this.form.factoryOvertimeCost ? Number(this.form.factoryOvertimeCost) : 0,
              taxCost: this.form.taxCost ? Number(this.form.taxCost) : 0,
              supplyTrips: this.form.supplyTrips ? Number(this.form.supplyTrips) : 0
            }

            // 发请求
            if (this.form.id) {
              request.put('/delivery-orders', formData)
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
            } else {
              request.post('/delivery-orders', formData)
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
          } catch (error) {
            console.error('保存失败：', error)
            this.$message.error(error.message || '保存失败')
            this.submitLoading = false
          }
        }
      })
    },

    // 重置表单
    resetForm() {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
      this.form = {
        orderDate: '',
        projectName: '',
        pouringPosition: '',
        receivingUnit: '',
        vehicleNumber: '',
        driverName: '',
        mixingStation: '',
        supplyGrade: '',
        supplyVolume: '',
        supplyTrips: '',
        supplyUnitPrice: '',
        supplyAmount: '',
        supplyOvertimeCost: '',
        extraFreightCost: '',
        freightCost: '',
        taxCost: '',
        supplyImage: '',
        factoryGrade: '',
        factoryVolume: '',
        factoryUnitPrice: '',
        factoryAmount: '',
        factoryOvertimeCost: '',
        factoryImage: '',
        remarks: '',
        supplyReconciliationStatus: '未对账',
        supplyPaymentStatus: '未收款',
        factoryReconciliationStatus: '未对账',
        factoryPaymentStatus: '未付款'
      }
      this.supplyImageList = []
      this.factoryImageList = []
    },

    // 图片上传相关方法
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

    handleUploadError(err) {
      console.error('上传失败：', err);
      this.$message.error('上传失败，请稍后重试');
    },

    beforeImageUpload(file) {
      const isImage = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isImage) {
        this.$message.error('只能上传JPG/PNG格式的图片!');
        return false;
      }
      if (!isLt2M) {
        this.$message.error('片大小不能超过2MB!');
        return false;
      }
      return true;
    },

    // 处理查询按钮点击
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },

    // 重置查询条件
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        startDate: '',
        endDate: '',
        projectName: '',
        receivingUnit: '',
        vehicleNumber: '',
        driverName: '',
        mixingStation: '',
        supplyReconciliationStatus: '',
        supplyPaymentStatus: '',
        factoryReconciliationStatus: '',
        factoryPaymentStatus: ''
      }
      this.getList()
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

    // 计算方量差异
    calculateDifference() {
      const supplyVolume = parseFloat(this.form.supplyVolume) || 0
      const factoryVolume = parseFloat(this.form.factoryVolume) || 0
      const supplyAmount = parseFloat(this.form.supplyAmount) || 0
      const factoryAmount = parseFloat(this.form.factoryAmount) || 0

      this.form.volumeDifference = (supplyVolume - factoryVolume).toFixed(2)
      this.form.amountDifference = (supplyAmount - factoryAmount).toFixed(2)
    },

    // 计算金额差异
    calculateAmountDifference() {
      if (this.form.supplyAmount && this.form.factoryAmount) {
        this.form.amountDifference = (this.form.supplyAmount - this.form.factoryAmount).toFixed(2)
      }
    },

    // 获取状态提示
    getStatusTooltip(type, status) {
      return `点击切换${type}状态`
    },

    // 处理导出按钮点击
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
        
        const response = await request.post('/delivery-orders/export', this.queryParams, {
          responseType: 'blob'
        })
        
        const blob = new Blob([response], { type: 'application/vnd.ms-excel' })
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = '货单数据.xlsx'
        link.click()
        window.URL.revokeObjectURL(link.href)
        
        this.$message.success('导出成功')
      } catch (error) {
        console.error('导出失败：', error)
        this.$message.error('导出失败，稍后重试')
      }
    },

    // 修改计算利润的方法
    calculateProfit() {
      // 收入 = 供货金额 + 补运费 + 供货超时费
      const supplyAmount = parseFloat(this.form.supplyAmount) || 0
      const extraFreightCost = parseFloat(this.form.extraFreightCost) || 0
      const supplyOvertimeCost = parseFloat(this.form.supplyOvertimeCost) || 0
      const totalIncome = supplyAmount + extraFreightCost + supplyOvertimeCost

      // 支出 = 厂金额 + 运费 + 出厂超时费 + 税费
      const factoryAmount = parseFloat(this.form.factoryAmount) || 0
      const freightCost = parseFloat(this.form.freightCost) || 0
      const factoryOvertimeCost = parseFloat(this.form.factoryOvertimeCost) || 0
      const taxCost = parseFloat(this.form.taxCost) || 0
      const totalCost = factoryAmount + freightCost + factoryOvertimeCost + taxCost

      // 利润 = 收入 - 支出
      this.form.profit = (totalIncome - totalCost).toFixed(2)
    },

    // 获取差异值的样式类
    getDifferenceClass(value) {
      if (!value) return '';
      const num = parseFloat(value);
      return num > 0 ? 'positive' : num < 0 ? 'negative' : '';
    },

    // 处理图片预览
    handlePreviewImage(url) {
      if (!url) return
      // el-image组件会自动处理预览
    },

    // 格式化数字，保2位小数
    formatNumber(num) {
      return num ? Number(num).toFixed(2) : '0.00'
    },
    
    // 计算汇总数据
    calculateSummary() {
      this.summaryData = this.orderList.reduce((sum, item) => {
        return {
          totalSupplyVolume: sum.totalSupplyVolume + (Number(item.supplyVolume) || 0),
          totalSupplyAmount: sum.totalSupplyAmount + (Number(item.supplyAmount) || 0),
          totalFactoryVolume: sum.totalFactoryVolume + (Number(item.factoryVolume) || 0),
          totalFactoryAmount: sum.totalFactoryAmount + (Number(item.factoryAmount) || 0),
          totalFreightCost: sum.totalFreightCost + (Number(item.freightCost) || 0),
          totalTaxCost: sum.totalTaxCost + (Number(item.taxCost) || 0),
          totalVolumeDifference: sum.totalVolumeDifference + (Number(item.volumeDifference) || 0),
          totalProfit: sum.totalProfit + (Number(item.profit) || 0)
        }
      }, {
        totalSupplyVolume: 0,
        totalSupplyAmount: 0,
        totalFactoryVolume: 0,
        totalFactoryAmount: 0,
        totalFreightCost: 0,
        totalTaxCost: 0,
        totalVolumeDifference: 0,
        totalProfit: 0
      })
    }
  },
  computed: {
    // 自动计算供货金额
    computedSupplyAmount() {
      const volume = parseFloat(this.form.supplyVolume) || 0
      const price = parseFloat(this.form.supplyUnitPrice) || 0
      return (volume * price).toFixed(2)
    },
    // 自动计算出厂金额
    computedFactoryAmount() {
      const volume = parseFloat(this.form.factoryVolume) || 0
      const price = parseFloat(this.form.factoryUnitPrice) || 0
      return (volume * price).toFixed(2)
    },
    uploadHeaders() {
      return {
        'Authorization': localStorage.getItem('token')
      }
    }
  },
  watch: {
    // 监听供货方量和单价，自动计算金额
    'form.supplyVolume'() {
      this.form.supplyAmount = this.computedSupplyAmount
      this.calculateDifference()
    },
    'form.supplyUnitPrice'() {
      this.form.supplyAmount = this.computedSupplyAmount
      this.calculateDifference()
    },
    // 监听出厂方量和单价，自动计算金额
    'form.factoryVolume'() {
      this.form.factoryAmount = this.computedFactoryAmount
      this.calculateDifference()
    },
    'form.factoryUnitPrice'() {
      this.form.factoryAmount = this.computedFactoryAmount
      this.calculateDifference()
    },
    // 监听所有影响利润的字段变化
    'form.supplyAmount'() { this.calculateProfit() },
    'form.extraFreightCost'() { this.calculateProfit() },
    'form.supplyOvertimeCost'() { this.calculateProfit() },
    'form.factoryAmount'() { this.calculateProfit() },
    'form.freightCost'() { this.calculateProfit() },
    'form.factoryOvertimeCost'() { this.calculateProfit() },
    'form.taxCost'() { this.calculateProfit() }
  }
}
</script>

<style lang="less" scoped>
.delivery-order {
  padding: 20px;
  background-color: #f0f2f5;
  min-height: calc(100vh - 84px);

  .search-form {
    background: #fff;
    padding: 22px 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
    margin-bottom: 20px;

    .el-form-item {
      margin-bottom: 18px;
    }
  }

  .el-table {
    margin-top: 20px;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);

    .info-cell {
      padding: 8px;
      .info-item {
        display: flex;
        justify-content: space-between;
        line-height: 24px;
        margin-bottom: 4px;
        
        &:last-child {
          margin-bottom: 0;
        }

        .label {
          color: #606266;
          font-size: 13px;
          min-width: 70px;
        }

        .value {
          color: #303133;
          font-weight: 500;
          flex: 1;
          text-align: right;
          padding-left: 10px;

          &.positive {
            color: #67C23A;
          }
          &.negative {
            color: #F56C6C;
          }
        }
      }
    }
  }

  .status-button {
    padding: 4px 12px;
    font-size: 12px;
    border-radius: 12px;
    margin: 2px;
    min-width: 70px;
    transition: all 0.3s;
    
    &.status-button--done {
      background-color: #67C23A;
      border-color: #67C23A;
      color: white;
      
      &:hover {
        background-color: #85ce61;
        border-color: #85ce61;
        transform: scale(1.05);
      }
    }
    
    &.status-button--pending {
      background-color: #E6A23C;
      border-color: #E6A23C;
      color: white;
      
      &:hover {
        background-color: #ebb563;
        border-color: #ebb563;
        transform: scale(1.05);
      }
    }

    &:active {
      transform: scale(0.95);
    }
  }

  .status-group {
    display: flex;
    gap: 8px;
    justify-content: center;
  }

  .table-image {
    width: 60px;
    height: 60px;
    border-radius: 4px;
    cursor: zoom-in;
    transition: all 0.3s;
    object-fit: cover;
    
    &:hover {
      transform: scale(1.1);
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
  }

  .el-pagination {
    margin-top: 20px;
    padding: 20px;
    text-align: right;
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
  }
}

.delivery-dialog {
  .form-section {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 24px;
    margin-bottom: 24px;

    .section-title {
      font-size: 16px;
      font-weight: bold;
      color: #303133;
      margin-bottom: 24px;
      padding-left: 12px;
      border-left: 4px solid #409EFF;
      display: flex;
      align-items: center;

      &::before {
        content: '';
        display: inline-block;
        width: 4px;
        height: 16px;
        background-color: #409EFF;
        margin-right: 8px;
        border-radius: 2px;
      }
    }
  }

  .el-form-item {
    margin-bottom: 22px;
  }

  .upload-demo {
    .el-upload {
      width: 100%;
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
      transition: all 0.3s;

      &:hover {
        border-color: #409EFF;
      }
    }

    .el-upload-list {
      width: 100%;
    }

    .el-upload__tip {
      color: #909399;
      font-size: 12px;
      margin-top: 8px;
    }
  }
}

.upload-demo {
  .el-upload {
    width: 100%;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: all 0.3s;

    &:hover {
      border-color: #409EFF;
    }
  }

  .el-upload-list {
    width: 100%;
  }

  .el-upload__tip {
    color: #909399;
    font-size: 12px;
    margin-top: 8px;
  }
}

.image-group {
  display: flex;
  gap: 15px;
  
  .image-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .image-label {
      font-size: 12px;
      color: #606266;
      margin-bottom: 5px;
    }
    
    .image-preview {
      cursor: zoom-in;
      border-radius: 4px;
      overflow: hidden;
      transition: all 0.3s;
      
      &:hover {
        transform: scale(1.05);
        box-shadow: 0 2px 12px 0 rgba(0,0,0,0.1);
      }
      
      .table-image {
        width: 60px;
        height: 60px;
        display: block;
      }
    }
    
    .no-image {
      width: 60px;
      height: 60px;
      display: flex;
      flex-direction: column;
      justify-content: center;
      align-items: center;
      background-color: #f5f7fa;
      border-radius: 4px;
      color: #909399;
      
      i {
        font-size: 20px;
        margin-bottom: 4px;
      }
      
      span {
        font-size: 12px;
      }
    }
  }
}

.image-slot {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
  background: #f5f7fa;
  color: #909399;
  font-size: 20px;
}

.search-form {
  .el-form-item {
    margin-bottom: 10px;
    margin-right: 10px;
  }
  
  .el-date-editor {
    width: 150px;
  }
  
  .el-select {
    width: 120px;
  }
}

.info-group {
  font-size: 12px;
  line-height: 1.4;
  
  .info-row {
    display: flex;
    margin-bottom: 4px;
    align-items: center;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .label {
      color: #909399;
      min-width: 70px;
      margin-right: 4px;
    }
    
    .value {
      color: #303133;
      flex: 1;
      
      &.positive {
        color: #67C23A;
      }
      
      &.negative {
        color: #F56C6C;
      }
    }
  }
}

.status-group {
  .status-row {
    display: flex;
    gap: 4px;
    margin-bottom: 4px;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .el-tag {
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        transform: scale(1.05);
      }
      
      &:active {
        transform: scale(0.95);
      }
    }
  }
}

.el-table {
  .cell {
    padding: 8px;
  }
  
  td {
    padding: 4px 0;
  }
}

.status-buttons {
  .status-row {
    display: flex;
    gap: 5px;
    margin-bottom: 5px;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    .status-btn {
      flex: 1;
      padding: 5px 8px;
      font-size: 12px;
      font-weight: bold;
      transition: all 0.3s cubic-bezier(.645,.045,.355,1);
      border-width: 2px;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 2px 8px rgba(0,0,0,0.15);
      }
      
      &:active {
        transform: translateY(1px);
      }
      
      // 已完成状态（绿色）
      &.el-button--success {
        background-color: #13CE66;
        border-color: #13CE66;
        
        &:hover, &:focus {
          background-color: #36D57D;
          border-color: #36D57D;
        }
        
        &:active {
          background-color: #0CA853;
          border-color: #0CA853;
        }
      }
      
      // 未完成状态（红色）
      &.el-button--danger {
        background-color: #FF4949;
        border-color: #FF4949;
        
        &:hover, &:focus {
          background-color: #FF6D6D;
          border-color: #FF6D6D;
        }
        
        &:active {
          background-color: #E63939;
          border-color: #E63939;
        }
      }
      
      // 按钮文字颜色和阴影效果
      &.el-button--success,
      &.el-button--danger {
        color: white;
        text-shadow: 0 -1px 0 rgba(0,0,0,0.15);
        box-shadow: 0 2px 0 rgba(0,0,0,0.045);
        
        &:hover, &:focus {
          color: white;
          box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }
      }
    }
  }
}

.summary-section {
  background: #fff;
  margin-top: 20px;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.05);
  
  .summary-title {
    font-size: 16px;
    font-weight: bold;
    color: #303133;
    margin-bottom: 16px;
    padding-left: 10px;
    border-left: 4px solid #409EFF;
  }
  
  .summary-content {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    
    .summary-item {
      min-width: 200px;
      display: flex;
      align-items: center;
      
      .label {
        color: #606266;
        margin-right: 8px;
        font-size: 14px;
        min-width: 100px;
      }
      
      .value {
        color: #303133;
        font-weight: 500;
        font-size: 14px;
        
        &.positive {
          color: #67C23A;
        }
        
        &.negative {
          color: #F56C6C;
        }
      }
    }
  }
}

// 确保分页在汇总信息下方
.el-pagination {
  margin-top: 20px;
}
</style> 