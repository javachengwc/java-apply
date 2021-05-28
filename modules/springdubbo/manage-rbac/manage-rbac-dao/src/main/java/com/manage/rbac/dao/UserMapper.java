package com.manage.rbac.dao;

import com.manage.rbac.entity.User;
import com.manage.rbac.entity.UserExample;
import org.springframework.stereotype.Repository;

/**
 * UserMapper继承基类
 */
@Repository
public interface UserMapper extends MyBatisBaseDao<User, Integer, UserExample> {
}