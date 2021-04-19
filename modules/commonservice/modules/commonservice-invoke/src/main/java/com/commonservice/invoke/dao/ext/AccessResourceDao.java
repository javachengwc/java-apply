package com.commonservice.invoke.dao.ext;

import com.commonservice.invoke.model.entity.AccessResource;
import com.commonservice.invoke.model.param.ResourceQuery;
import java.util.List;

public interface AccessResourceDao {

    public int countPage(ResourceQuery resourceQuery);

    public List<AccessResource> listPage(ResourceQuery resourceQuery);

}
