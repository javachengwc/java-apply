package com.manage.rbac.model.param;

import com.manage.rbac.model.vo.CrowdSimpleVO;
import com.manage.rbac.model.vo.PostSimpleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel(description = "机构参数", value = "orgReq")
public class OrgReq {

    @ApiModelProperty("机构id")
    private Integer id;

    @ApiModelProperty("机构名称")
    private String name;

    @ApiModelProperty("岗位列表")
    private List<PostSimpleVO> postList= Collections.EMPTY_LIST;

    @ApiModelProperty("用户组列表")
    private List<CrowdSimpleVO> crowdList= Collections.EMPTY_LIST;
}
