package com.sharding.junior.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharding.junior.model.entity.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
}
