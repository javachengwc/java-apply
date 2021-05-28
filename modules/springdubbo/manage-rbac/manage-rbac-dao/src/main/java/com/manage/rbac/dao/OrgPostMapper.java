package com.manage.rbac.dao;

import com.manage.rbac.entity.OrgPost;
import com.manage.rbac.entity.OrgPostExample;
import org.springframework.stereotype.Repository;

/**
 * OrgPostMapper继承基类
 */
@Repository
public interface OrgPostMapper extends MyBatisBaseDao<OrgPost, Integer, OrgPostExample> {
}