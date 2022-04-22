package com.commonservice.invoke.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.commonservice.invoke.model.entity.AccessResource;
import com.commonservice.invoke.model.param.AccessResourceQuery;
import com.commonservice.invoke.model.vo.AccessResourceVo;
import com.util.page.Page;
import java.util.List;

public interface AccessResourceService extends IService<AccessResource> {

    public int countPage(AccessResourceQuery resourceQuery);

    public List<AccessResource> listPage(AccessResourceQuery resourceQuery);

    public Page<AccessResourceVo> page(AccessResourceQuery resourceQuery);

}
