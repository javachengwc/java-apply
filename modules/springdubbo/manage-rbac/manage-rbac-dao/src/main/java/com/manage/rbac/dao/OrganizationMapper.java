package com.manage.rbac.dao;

import com.manage.rbac.entity.Organization;
import com.manage.rbac.entity.OrganizationExample;
import org.springframework.stereotype.Repository;

/**
 * OrganizationMapper继承基类
 */
@Repository
public interface OrganizationMapper extends MyBatisBaseDao<Organization, Integer, OrganizationExample> {
}