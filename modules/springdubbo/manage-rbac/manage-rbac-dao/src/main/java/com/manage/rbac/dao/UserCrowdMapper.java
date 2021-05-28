package com.manage.rbac.dao;

import com.manage.rbac.entity.UserCrowd;
import com.manage.rbac.entity.UserCrowdExample;
import org.springframework.stereotype.Repository;

/**
 * UserCrowdMapper继承基类
 */
@Repository
public interface UserCrowdMapper extends MyBatisBaseDao<UserCrowd, Integer, UserCrowdExample> {
}