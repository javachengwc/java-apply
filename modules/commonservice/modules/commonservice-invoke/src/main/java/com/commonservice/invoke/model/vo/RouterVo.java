package com.commonservice.invoke.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class RouterVo implements Serializable {

    @ApiModelProperty("路由名字")
    private String name;

    @ApiModelProperty("路由地址")
    private String path;

    @ApiModelProperty("元信息")
    private MetaVo meta;

    @ApiModelProperty("是否隐藏路由，设置 true 的时候该路由不会再侧边栏出现")
    private boolean hidden;

    @ApiModelProperty("重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击")
    private String redirect;

    @ApiModelProperty("组件地址")
    private String component;

    @ApiModelProperty("路由参数：如 {\"id\": 1, \"name\": \"ry\"}")
    private String query;

    private Boolean alwaysShow;

    @ApiModelProperty("子路由")
    private List<RouterVo> children;

}
