package com.sharding.junior.dao.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sharding.junior.model.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
