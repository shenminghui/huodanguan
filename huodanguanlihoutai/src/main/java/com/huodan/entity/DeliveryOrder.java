package com.huodan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("delivery_order")
public class DeliveryOrder {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String orderDate;
    private String projectName;
    private String pouringPosition;
    private String receivingUnit;
    private String vehicleNumber;
    private String supplyGrade;
    private BigDecimal supplyVolume;
    private Integer supplyTrips;
    private BigDecimal supplyUnitPrice;
    private BigDecimal supplyAmount;
    private String supplyReconciliationStatus;
    private String supplyPaymentStatus;
    private String supplyImage;
    private String mixingStation;
    private BigDecimal freightCost;
    private BigDecimal extraFreightCost;
    private BigDecimal supplyOvertimeCost;
    private BigDecimal factoryOvertimeCost;
    private String factoryGrade;
    private BigDecimal factoryVolume;
    private BigDecimal factoryUnitPrice;
    private BigDecimal factoryAmount;
    private BigDecimal volumeDifference;
    private String factoryReconciliationStatus;
    private String factoryPaymentStatus;
    private String factoryImage;
    private BigDecimal taxCost;
    private BigDecimal profit;
    private String remarks;
    private String driverName;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String orderDateStr;
} 