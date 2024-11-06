package com.huodan.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huodan.common.Result;
import com.huodan.entity.MaterialDetail;
import com.huodan.model.MaterialDetailQuery;
import com.huodan.service.MaterialDetailService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.excel.EasyExcel;
import com.huodan.config.FileUploadConfig;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/material-details")
public class MaterialDetailController {
    
    private final MaterialDetailService materialDetailService;
    
    @Autowired
    public MaterialDetailController(MaterialDetailService materialDetailService) {
        this.materialDetailService = materialDetailService;
    }

    @PostMapping("/page")
    public Result<IPage<MaterialDetail>> getPage(@RequestBody MaterialDetailQuery query) {
        return new Result<>(true, "查询成功", materialDetailService.getMaterialDetailPage(query));
    }

    @PostMapping
    public Result<Void> save(@RequestBody MaterialDetail detail) {
        try {
            // 设置默认值
            if (detail.getSupplyTonnage() == null) detail.setSupplyTonnage(BigDecimal.ZERO);
            if (detail.getSupplyUnitPrice() == null) detail.setSupplyUnitPrice(BigDecimal.ZERO);
            if (detail.getSupplyAmount() == null) detail.setSupplyAmount(BigDecimal.ZERO);
            if (detail.getFactoryTonnage() == null) detail.setFactoryTonnage(BigDecimal.ZERO);
            if (detail.getFactoryUnitPrice() == null) detail.setFactoryUnitPrice(BigDecimal.ZERO);
            if (detail.getFactoryAmount() == null) detail.setFactoryAmount(BigDecimal.ZERO);

            // 设置默认状态
            if (detail.getSupplyReconciliationStatus() == null) detail.setSupplyReconciliationStatus("未对账");
            if (detail.getSupplyPaymentStatus() == null) detail.setSupplyPaymentStatus("未收款");
            if (detail.getFactoryReconciliationStatus() == null) detail.setFactoryReconciliationStatus("未对账");
            if (detail.getFactoryPaymentStatus() == null) detail.setFactoryPaymentStatus("未付款");

            materialDetailService.save(detail);
            return new Result<>(true, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result<>(false, "保存失败：" + e.getMessage());
        }
    }

    @PutMapping
    public Result<Void> update(@RequestBody MaterialDetail detail) {
        materialDetailService.updateById(detail);
        return new Result<>(true, "更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        materialDetailService.removeById(id);
        return new Result<>(true, "删除成功");
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam String field,
            @RequestParam String status) {
        materialDetailService.updateStatus(id, field, status);
        return new Result<>(true, "状态更新成功");
    }

    @PostMapping("/upload")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return new Result<>(false, "上传文件不能为空", null);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
            
            if (!".jpg".equalsIgnoreCase(extension) && 
                !".jpeg".equalsIgnoreCase(extension) && 
                !".png".equalsIgnoreCase(extension)) {
                return new Result<>(false, "只支持jpg、jpeg和png格式的图片", null);
            }

            String newFileName = UUID.randomUUID().toString() + extension;
            File uploadDir = new File(FileUploadConfig.UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            File destFile = new File(uploadDir.getAbsolutePath() + File.separator + newFileName);
            file.transferTo(destFile);
            
            String imageUrl = "/api/uploads/" + newFileName;
            return new Result<>(true, "上传成功", imageUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result<>(false, "文件上传失败：" + e.getMessage(), null);
        }
    }

    @PostMapping("/export")
    public void export(@RequestBody MaterialDetailQuery query, HttpServletResponse response) throws IOException {
        try {
            query.setPageNum(1);
            query.setPageSize(Integer.MAX_VALUE);
            List<MaterialDetail> list = materialDetailService.getExportList(query);
            
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("建筑材料明细", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            
            List<List<String>> headers = getExportHeaders();
            List<List<Object>> dataList = new ArrayList<>();
            
            for (MaterialDetail detail : list) {
                List<Object> rowData = new ArrayList<>();
                rowData.add(detail.getDetailDate());
                rowData.add(detail.getReceivingLocation());
                rowData.add(detail.getMaterialType());
                rowData.add(detail.getTrips());
                rowData.add(detail.getVehicleNumber());
                rowData.add(detail.getSupplyTonnage().setScale(2, RoundingMode.HALF_UP));
                rowData.add(detail.getSupplyUnitPrice().setScale(2, RoundingMode.HALF_UP));
                rowData.add(detail.getSupplyAmount().setScale(2, RoundingMode.HALF_UP));
                rowData.add(detail.getSupplyReconciliationStatus());
                rowData.add(detail.getSupplyPaymentStatus());
                rowData.add(detail.getFactoryTonnage().setScale(2, RoundingMode.HALF_UP));
                rowData.add(detail.getFactoryUnitPrice().setScale(2, RoundingMode.HALF_UP));
                rowData.add(detail.getFactoryAmount().setScale(2, RoundingMode.HALF_UP));
                rowData.add(detail.getFactoryReconciliationStatus());
                rowData.add(detail.getFactoryPaymentStatus());
                rowData.add(detail.getRemarks());
                dataList.add(rowData);
            }
            
            EasyExcel.write(response.getOutputStream())
                .head(headers)
                .sheet("建筑材料明细")
                .doWrite(dataList);
            
        } catch (Exception e) {
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().println(JSONObject.toJSONString(new Result<>(false, "导出失败：" + e.getMessage())));
        }
    }

    private List<List<String>> getExportHeaders() {
        List<List<String>> headers = new ArrayList<>();
        headers.add(Collections.singletonList("日期"));
        headers.add(Collections.singletonList("收货地"));
        headers.add(Collections.singletonList("材料类型"));
        headers.add(Collections.singletonList("车次"));
        headers.add(Collections.singletonList("车号"));
        headers.add(Collections.singletonList("供货吨数"));
        headers.add(Collections.singletonList("供货单价"));
        headers.add(Collections.singletonList("供货金额"));
        headers.add(Collections.singletonList("供货对账状态"));
        headers.add(Collections.singletonList("供货收款状态"));
        headers.add(Collections.singletonList("出厂吨数"));
        headers.add(Collections.singletonList("出厂单价"));
        headers.add(Collections.singletonList("出厂金额"));
        headers.add(Collections.singletonList("出厂对账状态"));
        headers.add(Collections.singletonList("出厂付款状态"));
        headers.add(Collections.singletonList("备注"));
        return headers;
    }
} 