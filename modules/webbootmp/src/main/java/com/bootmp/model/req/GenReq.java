package com.bootmp.model.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "genReq", description = "自动生成代码请求参数类")
public class GenReq {

    @ApiModelProperty("数据库账号")
    private String userName;

    @ApiModelProperty("数据库密码")
    private String password;

    @ApiModelProperty("数据库url")
    private String url;

    @ApiModelProperty("项目地址")
    private String projectPath;

    @ApiModelProperty("项目的包")
    private String projectPackage;

    @ApiModelProperty("核心模块的包")
    private String corePackage;

    @ApiModelProperty("作者")
    private String author;

    @ApiModelProperty("表名")
    private String tableName;

    @ApiModelProperty("忽略的表前缀")
    private String ignoreTabelPrefix;

    @ApiModelProperty("业务名称")
    private String bizName;

    @ApiModelProperty("模块名")
    private String moduleName;

    @ApiModelProperty("父级菜单名称")
    private String parentMenuName;

    @ApiModelProperty("是否生成实体代码开关")
    private Boolean entitySwitch = false;

    @ApiModelProperty("是否生成控制层代码开关")
    private Boolean controllerSwitch = false;

    @ApiModelProperty("是否生成服务层代码开关")
    private Boolean serviceSwitch = false;

    @ApiModelProperty("是否生成dao层代码开关")
    private Boolean daoSwitch = false;

    @ApiModelProperty("是否生成sql代码开关")
    private Boolean sqlSwitch = false;

    @ApiModelProperty("主页")
    private Boolean indexPageSwitch = false;

    @ApiModelProperty("主页的js")
    private Boolean jsSwitch = false;

    @ApiModelProperty("添加页面")
    private Boolean addPageSwitch = false;

    @ApiModelProperty("编辑页面")
    private Boolean editPageSwitch = false;

    @ApiModelProperty("详情页面js")
    private Boolean infoJsSwitch = false;

}
