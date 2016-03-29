package com.configcenter.vo;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.List;
import java.util.Map;

/**
 * 菜单树节点
 * 与jquery easyui tree节点结构保持一致
 */
public class TreeNode {

    //节点id
    private Integer id;

    //父id
    private Integer pid;

    //是否父节点 0--不是 1--是
    private Integer isParent=0;

    //节点名
    private String text;

    //路径
    private String url;

    //是否选中
    private boolean checked;

    //子节点
    private List<TreeNode> children;

    //节点扩展属性
    private Map<String,String> attributes;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getIsParent() {
        return isParent;
    }

    public void setIsParent(Integer isParent) {
        this.isParent = isParent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public Map<String, String> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, String> attributes) {
        this.attributes = attributes;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
