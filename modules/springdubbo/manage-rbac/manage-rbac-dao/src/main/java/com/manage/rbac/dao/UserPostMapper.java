package com.manage.rbac.dao;

import com.manage.rbac.entity.UserPost;
import com.manage.rbac.entity.UserPostExample;
import org.springframework.stereotype.Repository;

/**
 * UserPostMapper继承基类
 */
@Repository
public interface UserPostMapper extends MyBatisBaseDao<UserPost, Integer, UserPostExample> {
}