package com.manage.rbac.model.param;

import com.manage.rbac.model.vo.OrgSimpleVO;
import com.manage.rbac.model.vo.RoleSimpleVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collections;
import java.util.List;

@Data
@ApiModel(description = "岗位参数", value = "postReq")
public class PostReq {

    @ApiModelProperty("岗位id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("机构列表")
    private List<OrgSimpleVO> orgList= Collections.EMPTY_LIST;

    @ApiModelProperty("角色列表")
    private List<RoleSimpleVO> roleList= Collections.EMPTY_LIST;
}
