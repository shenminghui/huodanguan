package com.huodan.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huodan.entity.DeliveryOrder;
import com.huodan.mapper.DeliveryOrderMapper;
import com.huodan.model.DeliveryOrderQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
public class DeliveryOrderService extends ServiceImpl<DeliveryOrderMapper, DeliveryOrder> {
    
    @Autowired
    private DeliveryOrderMapper deliveryOrderMapper;

    public IPage<DeliveryOrder> getDeliveryOrderPage(DeliveryOrderQuery query) {
        Page<DeliveryOrder> page = new Page<>(query.getPageNum(), query.getPageSize());
        
        QueryWrapper<DeliveryOrder> wrapper = new QueryWrapper<>();
        
        if (query.getStartDate() != null && !query.getStartDate().isEmpty()) {
            wrapper.ge("order_date", query.getStartDate());
        }
        if (query.getEndDate() != null && !query.getEndDate().isEmpty()) {
            wrapper.le("order_date", query.getEndDate());
        }
        if (query.getProjectName() != null && !query.getProjectName().isEmpty()) {
            wrapper.like("project_name", query.getProjectName());
        }
        if (query.getReceivingUnit() != null && !query.getReceivingUnit().isEmpty()) {
            wrapper.like("receiving_unit", query.getReceivingUnit());
        }
        if (query.getVehicleNumber() != null && !query.getVehicleNumber().isEmpty()) {
            wrapper.eq("vehicle_number", query.getVehicleNumber());
        }
        if (query.getDriverName() != null && !query.getDriverName().isEmpty()) {
            wrapper.like("driver_name", query.getDriverName());
        }
        if (query.getMixingStation() != null && !query.getMixingStation().isEmpty()) {
            wrapper.like("mixing_station", query.getMixingStation());
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

        wrapper.orderByDesc("STR_TO_DATE(order_date, '%Y/%m/%d')", "id");

        return this.page(page, wrapper);
    }

    public void updateStatus(Long id, String field, String status) {
        DeliveryOrder order = new DeliveryOrder();
        order.setId(id);
        switch (field) {
            case "supplyReconciliation":
                order.setSupplyReconciliationStatus(status);
                break;
            case "supplyPayment":
                order.setSupplyPaymentStatus(status);
                break;
            case "factoryReconciliation":
                order.setFactoryReconciliationStatus(status);
                break;
            case "factoryPayment":
                order.setFactoryPaymentStatus(status);
                break;
        }
        updateById(order);
    }

    public List<DeliveryOrder> getExportList(DeliveryOrderQuery query) {
        QueryWrapper<DeliveryOrder> wrapper = new QueryWrapper<>();
        
        if (query.getStartDate() != null && !query.getStartDate().isEmpty()) {
            wrapper.ge("order_date", query.getStartDate());
        }
        if (query.getEndDate() != null && !query.getEndDate().isEmpty()) {
            wrapper.le("order_date", query.getEndDate());
        }
        
        if (query.getProjectName() != null && !query.getProjectName().isEmpty()) {
            wrapper.like("project_name", query.getProjectName());
        }
        if (query.getReceivingUnit() != null && !query.getReceivingUnit().isEmpty()) {
            wrapper.like("receiving_unit", query.getReceivingUnit());
        }
        if (query.getVehicleNumber() != null && !query.getVehicleNumber().isEmpty()) {
            wrapper.eq("vehicle_number", query.getVehicleNumber());
        }
        if (query.getDriverName() != null && !query.getDriverName().isEmpty()) {
            wrapper.like("driver_name", query.getDriverName());
        }
        if (query.getMixingStation() != null && !query.getMixingStation().isEmpty()) {
            wrapper.like("mixing_station", query.getMixingStation());
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

        wrapper.orderByDesc("order_date", "id");
        
        return list(wrapper);
    }
} 