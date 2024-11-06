package com.huodan.model;

import lombok.Data;

@Data
public class MaterialDetailQuery {
    private String startDate;
    private String endDate;
    private String receivingLocation;
    private String materialType;
    private String vehicleNumber;
    private String supplyReconciliationStatus;
    private String supplyPaymentStatus;
    private String factoryReconciliationStatus;
    private String factoryPaymentStatus;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
} 