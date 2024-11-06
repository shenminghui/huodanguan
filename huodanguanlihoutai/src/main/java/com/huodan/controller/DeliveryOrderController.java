package com.huodan.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huodan.common.Result;
import com.huodan.entity.DeliveryOrder;
import com.huodan.model.DeliveryOrderQuery;
import com.huodan.service.DeliveryOrderService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.List;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.huodan.config.FileUploadConfig;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/delivery-orders")
public class DeliveryOrderController {
    private final DeliveryOrderService deliveryOrderService;
    
    @Autowired
    public DeliveryOrderController(DeliveryOrderService deliveryOrderService) {
        this.deliveryOrderService = deliveryOrderService;
    }

    @PostMapping("/page")
    public Result<IPage<DeliveryOrder>> getPage(@RequestBody DeliveryOrderQuery query) {
        return new Result<>(true, "查询成功", deliveryOrderService.getDeliveryOrderPage(query));
    }

    @PostMapping
    public Result<Void> save(@RequestBody DeliveryOrder order) {
        try {
            // 设置默认值
            if (order.getSupplyVolume() == null) order.setSupplyVolume(BigDecimal.ZERO);
            if (order.getSupplyUnitPrice() == null) order.setSupplyUnitPrice(BigDecimal.ZERO);
            if (order.getSupplyAmount() == null) order.setSupplyAmount(BigDecimal.ZERO);
            if (order.getSupplyOvertimeCost() == null) order.setSupplyOvertimeCost(BigDecimal.ZERO);
            if (order.getExtraFreightCost() == null) order.setExtraFreightCost(BigDecimal.ZERO);
            if (order.getFreightCost() == null) order.setFreightCost(BigDecimal.ZERO);
            if (order.getFactoryVolume() == null) order.setFactoryVolume(BigDecimal.ZERO);
            if (order.getFactoryUnitPrice() == null) order.setFactoryUnitPrice(BigDecimal.ZERO);
            if (order.getFactoryAmount() == null) order.setFactoryAmount(BigDecimal.ZERO);
            if (order.getFactoryOvertimeCost() == null) order.setFactoryOvertimeCost(BigDecimal.ZERO);
            if (order.getTaxCost() == null) order.setTaxCost(BigDecimal.ZERO);
            
            // 计算差异和利润
            BigDecimal volumeDifference = order.getSupplyVolume().subtract(order.getFactoryVolume());
            order.setVolumeDifference(volumeDifference);
            
            // 计算利润
            BigDecimal income = order.getSupplyAmount()
                .add(order.getExtraFreightCost())
                .add(order.getSupplyOvertimeCost());
            
            BigDecimal cost = order.getFactoryAmount()
                .add(order.getFreightCost())
                .add(order.getFactoryOvertimeCost())
                .add(order.getTaxCost());
            
            BigDecimal profit = income.subtract(cost);
            order.setProfit(profit);
            
            // 设置默认状态
            if (order.getSupplyReconciliationStatus() == null) order.setSupplyReconciliationStatus("未对账");
            if (order.getSupplyPaymentStatus() == null) order.setSupplyPaymentStatus("未收款");
            if (order.getFactoryReconciliationStatus() == null) order.setFactoryReconciliationStatus("未对账");
            if (order.getFactoryPaymentStatus() == null) order.setFactoryPaymentStatus("未付款");

            deliveryOrderService.save(order);
            return new Result<>(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(false, "保存失败：" + e.getMessage());
        }
    }

    @PutMapping
    public Result<Void> update(@RequestBody DeliveryOrder order) {
        deliveryOrderService.updateById(order);
        return new Result<>(true, "更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        deliveryOrderService.removeById(id);
        return new Result<>(true, "删除成功");
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam String field,
            @RequestParam String status) {
        deliveryOrderService.updateStatus(id, field, status);
        return new Result<>(true, "状态更新成功");
    }

    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // 检查文件是否为空
            if (file.isEmpty()) {
                return new Result<>(false, "上传文件不能为空", null);
            }

            // 获取文件名和扩展名
            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            // 检查文件类型
            if (!".jpg".equalsIgnoreCase(extension) && 
                !".jpeg".equalsIgnoreCase(extension) && 
                !".png".equalsIgnoreCase(extension)) {
                return new Result<>(false, "只支持jpg、jpeg和png格式的图片", null);
            }

            // 生成新的文件名
            String newFileName = UUID.randomUUID().toString() + extension;
            
            // 确保上传目录存在
            File uploadDir = new File(FileUploadConfig.UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            // 创建目标文件
            File destFile = new File(uploadDir.getAbsolutePath() + File.separator + newFileName);
            
            // 保存文件
            file.transferTo(destFile);
            
            // 返回完整的访问URL
            String imageUrl = "/api/uploads/" + newFileName;
            return new Result<>(true, "上传成功", imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result<>(false, "文件上传失败：" + e.getMessage(), null);
        }
    }

    @PostMapping("/export")
    public void export(@RequestBody DeliveryOrderQuery query, HttpServletResponse response) throws IOException {
        try {
            // 获取所有数据，不分页
            query.setPageNum(1);
            query.setPageSize(Integer.MAX_VALUE);
            List<DeliveryOrder> list = deliveryOrderService.getExportList(query);
            
            // 设置响应头
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("货单数据", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            
            // 创建表头
            List<List<String>> headers = getExportHeaders();
            
            // 创建数据行，保留2位小数
            List<List<Object>> dataList = new ArrayList<>();
            for (DeliveryOrder order : list) {
                List<Object> rowData = new ArrayList<>();
                rowData.add(order.getOrderDate());
                rowData.add(order.getProjectName());
                rowData.add(order.getPouringPosition());
                rowData.add(order.getReceivingUnit());
                rowData.add(order.getVehicleNumber());
                rowData.add(order.getDriverName());
                rowData.add(order.getMixingStation());
                rowData.add(order.getSupplyGrade());
                rowData.add(order.getSupplyVolume() != null ? order.getSupplyVolume().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getSupplyTrips());
                rowData.add(order.getSupplyUnitPrice() != null ? order.getSupplyUnitPrice().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getSupplyAmount() != null ? order.getSupplyAmount().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getSupplyOvertimeCost() != null ? order.getSupplyOvertimeCost().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getExtraFreightCost() != null ? order.getExtraFreightCost().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getSupplyReconciliationStatus());
                rowData.add(order.getSupplyPaymentStatus());
                rowData.add(order.getFactoryGrade());
                rowData.add(order.getFactoryVolume() != null ? order.getFactoryVolume().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getFactoryUnitPrice() != null ? order.getFactoryUnitPrice().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getFactoryAmount() != null ? order.getFactoryAmount().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getFactoryOvertimeCost() != null ? order.getFactoryOvertimeCost().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getFactoryReconciliationStatus());
                rowData.add(order.getFactoryPaymentStatus());
                rowData.add(order.getFreightCost() != null ? order.getFreightCost().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getTaxCost() != null ? order.getTaxCost().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getVolumeDifference() != null ? order.getVolumeDifference().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getProfit() != null ? order.getProfit().setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                rowData.add(order.getRemarks());
                dataList.add(rowData);
            }
            
            // 写入Excel
            EasyExcel.write(response.getOutputStream())
                .head(headers)
                .sheet("货单数据")
                .doWrite(dataList);
            
        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(JSONObject.toJSONString(new Result<>(false, "导出失败：" + e.getMessage())));
        }
    }

    // 获取Excel表头
    private List<List<String>> getExportHeaders() {
        List<List<String>> headers = new ArrayList<>();
        headers.add(Collections.singletonList("日期"));
        headers.add(Collections.singletonList("工程名称"));
        headers.add(Collections.singletonList("浇筑部位"));
        headers.add(Collections.singletonList("收货单位"));
        headers.add(Collections.singletonList("车号"));
        headers.add(Collections.singletonList("司机姓名"));
        headers.add(Collections.singletonList("商混站"));
        headers.add(Collections.singletonList("供货标号"));
        headers.add(Collections.singletonList("供货方量"));
        headers.add(Collections.singletonList("供货车次"));
        headers.add(Collections.singletonList("供货单价"));
        headers.add(Collections.singletonList("供货金额"));
        headers.add(Collections.singletonList("供货超时费"));
        headers.add(Collections.singletonList("补运费"));
        headers.add(Collections.singletonList("供货对账状态"));
        headers.add(Collections.singletonList("供货收款状态"));
        headers.add(Collections.singletonList("出厂标号"));
        headers.add(Collections.singletonList("出厂方量"));
        headers.add(Collections.singletonList("出厂单价"));
        headers.add(Collections.singletonList("出厂金额"));
        headers.add(Collections.singletonList("出厂超时费"));
        headers.add(Collections.singletonList("出厂对账状态"));
        headers.add(Collections.singletonList("出厂付款状态"));
        headers.add(Collections.singletonList("运费"));
        headers.add(Collections.singletonList("税费"));
        headers.add(Collections.singletonList("方量差异"));
        headers.add(Collections.singletonList("利润"));
        headers.add(Collections.singletonList("备注"));
        return headers;
    }
} 