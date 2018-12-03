package com.shop.book.manage.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;
import java.util.ArrayList;
import java.util.List;

@ApiModel(value = "menuVo", description = "菜单信息")
public class MenuVo {

    @ApiModelProperty("菜单ID")
    private Long id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("菜单url")
    private String url;

    @ApiModelProperty("菜单授权")
    private String perms;

    @ApiModelProperty("类型：0--目录，1--菜单，2--按钮")
    private Integer type;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("父菜单ID")
    private Long parentId;

    @ApiModelProperty("是否是导航栏，0--否 1--是")
    private Integer nav;

    @ApiModelProperty("子菜单集合")
    private List<MenuVo> children = new ArrayList<MenuVo>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPerms() {
        return perms;
    }

    public void setPerms(String perms) {
        this.perms = perms;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getNav() {
        return nav;
    }

    public void setNav(Integer nav) {
        this.nav = nav;
    }

    public List<MenuVo> getChildren() {
        return children;
    }

    public void setChildren(List<MenuVo> children) {
        this.children = children;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
