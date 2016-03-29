package com.configcenter.vo;

/**
 * jquery easyui treegrid
 * jquery easyui tree
 * 节点状态
 */
public enum NodeState {

    CLOSED("关闭","closed"),OPENED("展开","opened");

    private String name;

    private String value;

    private NodeState(String name,String value)
    {
        this.name=name;
        this.value=value;

    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
