package com.manage.rbac.dao;

import com.manage.rbac.entity.Crowd;
import com.manage.rbac.entity.CrowdExample;
import org.springframework.stereotype.Repository;

/**
 * CrowdMapper继承基类
 */
@Repository
public interface CrowdMapper extends MyBatisBaseDao<Crowd, Integer, CrowdExample> {
}