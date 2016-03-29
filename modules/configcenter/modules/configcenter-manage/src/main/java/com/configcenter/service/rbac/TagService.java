package com.configcenter.service.rbac;

import com.configcenter.dao.rbac.TagDao;
import com.configcenter.vo.CommonQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 权限标签服务类
 */
@Service
public class TagService {

    @Autowired
    private TagDao tagDao;

    public List<String> queryPage(CommonQueryVo queryVo)
    {
        return tagDao.queryPage(queryVo);
    }
}
