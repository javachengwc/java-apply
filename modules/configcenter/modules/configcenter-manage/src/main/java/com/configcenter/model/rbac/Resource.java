package com.configcenter.model.rbac;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 资源
 * (包括菜单与操作)
 */
public class Resource {

    private  Integer id;

    //名称
    private String name;

    //路径
    private String path;

    //父id
    private Integer parentId;

    //是否显示
    private Integer isShow;

    //是否菜单
    private Integer isMenu;

    //标签
    private String tag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsMenu() {
        return isMenu;
    }

    public void setIsMenu(Integer isMenu) {
        this.isMenu = isMenu;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
