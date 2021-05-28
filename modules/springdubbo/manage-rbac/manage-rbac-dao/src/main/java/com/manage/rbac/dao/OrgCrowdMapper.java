package com.manage.rbac.dao;

import com.manage.rbac.entity.OrgCrowd;
import com.manage.rbac.entity.OrgCrowdExample;
import org.springframework.stereotype.Repository;

/**
 * OrgCrowdMapper继承基类
 */
@Repository
public interface OrgCrowdMapper extends MyBatisBaseDao<OrgCrowd, Integer, OrgCrowdExample> {
}