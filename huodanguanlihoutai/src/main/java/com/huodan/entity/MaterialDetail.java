package com.huodan.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("material_detail")
public class MaterialDetail {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String detailDate;
    private String receivingLocation;
    private String materialType;
    private Integer trips;
    private String vehicleNumber;
    private BigDecimal supplyTonnage;
    private BigDecimal supplyUnitPrice;
    private BigDecimal supplyAmount;
    private String supplyReconciliationStatus;
    private String supplyPaymentStatus;
    private String supplyImage;
    private BigDecimal factoryTonnage;
    private BigDecimal factoryUnitPrice;
    private BigDecimal factoryAmount;
    private String factoryReconciliationStatus;
    private String factoryPaymentStatus;
    private String factoryImage;
    private String remarks;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
} 