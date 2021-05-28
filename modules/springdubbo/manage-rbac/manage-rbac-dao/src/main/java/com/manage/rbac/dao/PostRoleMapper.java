package com.manage.rbac.dao;

import com.manage.rbac.entity.PostRole;
import com.manage.rbac.entity.PostRoleExample;
import org.springframework.stereotype.Repository;

/**
 * PostRoleMapper继承基类
 */
@Repository
public interface PostRoleMapper extends MyBatisBaseDao<PostRole, Integer, PostRoleExample> {
}