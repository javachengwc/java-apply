package com.commonservice.invoke.dao.ext;

import com.commonservice.invoke.model.entity.AccessResource;
import com.commonservice.invoke.model.param.AccessResourceQuery;
import java.util.List;

public interface AccessResourceDao {

    public int countPage(AccessResourceQuery resourceQuery);

    public List<AccessResource> listPage(AccessResourceQuery resourceQuery);

}
