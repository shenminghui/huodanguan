package com.huodan.model;

import lombok.Data;

@Data
public class DeliveryOrderQuery {
    private String startDate;
    private String endDate;
    private String projectName;
    private String receivingUnit;
    private String vehicleNumber;
    private String supplyReconciliationStatus;
    private String supplyPaymentStatus;
    private String mixingStation;
    private String factoryReconciliationStatus;
    private String factoryPaymentStatus;
    private String driverName;
    private Integer pageNum = 1;
    private Integer pageSize = 10;
} 