package com.storefront.manage.dao;

import com.storefront.manage.model.vo.IndustryQueryVo;
import com.storefront.manage.model.vo.IndustryVo;
import java.util.List;

public interface IndustryDao {

    public List<IndustryVo> queryPage(IndustryQueryVo queryVo);
}
