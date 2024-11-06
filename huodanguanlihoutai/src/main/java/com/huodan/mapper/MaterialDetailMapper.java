package com.huodan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huodan.entity.MaterialDetail;
import com.huodan.model.MaterialDetailQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MaterialDetailMapper extends BaseMapper<MaterialDetail> {
    IPage<MaterialDetail> selectMaterialDetailPage(Page<MaterialDetail> page, @Param("query") MaterialDetailQuery query);
} 