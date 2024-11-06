package com.huodan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huodan.entity.MaterialDetail;
import com.huodan.mapper.MaterialDetailMapper;
import com.huodan.model.MaterialDetailQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class MaterialDetailService extends ServiceImpl<MaterialDetailMapper, MaterialDetail> {
    
    @Autowired
    private MaterialDetailMapper materialDetailMapper;

    public IPage<MaterialDetail> getMaterialDetailPage(MaterialDetailQuery query) {
        Page<MaterialDetail> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        QueryWrapper<MaterialDetail> wrapper = new QueryWrapper<>();
        
        if (query.getStartDate() != null && !query.getStartDate().isEmpty()) {
            wrapper.ge("detail_date", query.getStartDate());
        }
        if (query.getEndDate() != null && !query.getEndDate().isEmpty()) {
            wrapper.le("detail_date", query.getEndDate());
        }
        if (query.getReceivingLocation() != null && !query.getReceivingLocation().isEmpty()) {
            wrapper.like("receiving_location", query.getReceivingLocation());
        }
        if (query.getMaterialType() != null && !query.getMaterialType().isEmpty()) {
            wrapper.like("material_type", query.getMaterialType());
        }
        if (query.getVehicleNumber() != null && !query.getVehicleNumber().isEmpty()) {
            wrapper.eq("vehicle_number", query.getVehicleNumber());
        }
        if (query.getSupplyReconciliationStatus() != null && !query.getSupplyReconciliationStatus().isEmpty()) {
            wrapper.eq("supply_reconciliation_status", query.getSupplyReconciliationStatus());
        }
        if (query.getSupplyPaymentStatus() != null && !query.getSupplyPaymentStatus().isEmpty()) {
            wrapper.eq("supply_payment_status", query.getSupplyPaymentStatus());
        }
        if (query.getFactoryReconciliationStatus() != null && !query.getFactoryReconciliationStatus().isEmpty()) {
            wrapper.eq("factory_reconciliation_status", query.getFactoryReconciliationStatus());
        }
        if (query.getFactoryPaymentStatus() != null && !query.getFactoryPaymentStatus().isEmpty()) {
            wrapper.eq("factory_payment_status", query.getFactoryPaymentStatus());
        }

        wrapper.orderByDesc("detail_date", "id");

        return this.page(page, wrapper);
    }

    public void updateStatus(Long id, String field, String status) {
        MaterialDetail detail = new MaterialDetail();
        detail.setId(id);
        switch (field) {
            case "supplyReconciliation":
                detail.setSupplyReconciliationStatus(status);
                break;
            case "supplyPayment":
                detail.setSupplyPaymentStatus(status);
                break;
            case "factoryReconciliation":
                detail.setFactoryReconciliationStatus(status);
                break;
            case "factoryPayment":
                detail.setFactoryPaymentStatus(status);
                break;
        }
        updateById(detail);
    }

    public List<MaterialDetail> getExportList(MaterialDetailQuery query) {
        QueryWrapper<MaterialDetail> wrapper = new QueryWrapper<>();
        
        if (query.getStartDate() != null && !query.getStartDate().isEmpty()) {
            wrapper.ge("detail_date", query.getStartDate());
        }
        if (query.getEndDate() != null && !query.getEndDate().isEmpty()) {
            wrapper.le("detail_date", query.getEndDate());
        }
        if (query.getReceivingLocation() != null && !query.getReceivingLocation().isEmpty()) {
            wrapper.like("receiving_location", query.getReceivingLocation());
        }
        if (query.getMaterialType() != null && !query.getMaterialType().isEmpty()) {
            wrapper.like("material_type", query.getMaterialType());
        }
        if (query.getVehicleNumber() != null && !query.getVehicleNumber().isEmpty()) {
            wrapper.eq("vehicle_number", query.getVehicleNumber());
        }
        if (query.getSupplyReconciliationStatus() != null && !query.getSupplyReconciliationStatus().isEmpty()) {
            wrapper.eq("supply_reconciliation_status", query.getSupplyReconciliationStatus());
        }
        if (query.getSupplyPaymentStatus() != null && !query.getSupplyPaymentStatus().isEmpty()) {
            wrapper.eq("supply_payment_status", query.getSupplyPaymentStatus());
        }
        if (query.getFactoryReconciliationStatus() != null && !query.getFactoryReconciliationStatus().isEmpty()) {
            wrapper.eq("factory_reconciliation_status", query.getFactoryReconciliationStatus());
        }
        if (query.getFactoryPaymentStatus() != null && !query.getFactoryPaymentStatus().isEmpty()) {
            wrapper.eq("factory_payment_status", query.getFactoryPaymentStatus());
        }

        wrapper.orderByDesc("detail_date", "id");
        
        return list(wrapper);
    }
} 