package com.configcenter.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.configcenter.model.rbac.Resource;

/**
 * 资源vo
 * 与jquery easyui treegrid节点结构保持一致
 * fastjson 默认莫办法解析_开头的字段，需用注解标记，且必须是在get方法上
 */

public class ResourceVo  extends Resource {

    private Integer _parentId;

    private String state = NodeState.OPENED.getValue();

    @JSONField(name="_parentId")
    public Integer get_parentId() {
        return _parentId;
    }

    public void set_parentId(Integer _parentId) {
        this._parentId = _parentId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
