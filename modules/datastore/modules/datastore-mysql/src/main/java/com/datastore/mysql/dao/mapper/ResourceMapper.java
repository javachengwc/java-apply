package com.datastore.mysql.dao.mapper;

import com.datastore.mysql.model.Resource;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ResourceMapper {

    @Select("select * from sys_resource where name= #{name}")
    public Resource getByName(@Param("name") String name);

    @Select("select * from sys_resource where id= #{0}")
    public Resource getById(Integer id);

    @Select("select count(1) as cnt  from sys_resource")
    public Integer countAll();

    @Select("select * from sys_resource")
    public List<Resource> queryAll();
}
