package com.commonservice.invoke.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.commonservice.invoke.model.entity.AccessResource;
import com.commonservice.invoke.model.param.ResourceQuery;
import com.commonservice.invoke.model.vo.AccessResourceVo;
import com.util.page.Page;
import java.util.List;

public interface AccessResourceService extends IService<AccessResource> {

    public int countPage(ResourceQuery resourceQuery);

    public List<AccessResource> listPage(ResourceQuery resourceQuery);

    public Page<AccessResourceVo> page(ResourceQuery resourceQuery);

}
