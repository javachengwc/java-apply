package com.configcenter.dao.rbac;

import com.configcenter.vo.CommonQueryVo;

import java.util.List;

/**
 * 权限标签访问类
 */
public interface TagDao {

   public List<String> queryPage(CommonQueryVo queryVo);

}
