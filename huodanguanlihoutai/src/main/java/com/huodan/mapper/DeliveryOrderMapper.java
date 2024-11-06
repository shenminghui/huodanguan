package com.huodan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huodan.entity.DeliveryOrder;
import com.huodan.model.DeliveryOrderQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface DeliveryOrderMapper extends BaseMapper<DeliveryOrder> {
    IPage<DeliveryOrder> selectDeliveryOrderPage(Page<DeliveryOrder> page, @Param("query") DeliveryOrderQuery query);
} 