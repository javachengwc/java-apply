package com.commonservice.invoke.model.param;

import com.commonservice.invoke.model.vo.AccessResourceVo;
import com.util.page.PageQuery;
import java.io.Serializable;
import lombok.Data;

@Data
public class ResourceQuery extends PageQuery<AccessResourceVo> implements Serializable {

    private String createTimeBegin;

    private String createTimeEnd;

}
