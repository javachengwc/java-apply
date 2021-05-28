package com.manage.rbac.dao;

import com.manage.rbac.entity.Post;
import com.manage.rbac.entity.PostExample;
import org.springframework.stereotype.Repository;

/**
 * PostMapper继承基类
 */
@Repository
public interface PostMapper extends MyBatisBaseDao<Post, Integer, PostExample> {
}