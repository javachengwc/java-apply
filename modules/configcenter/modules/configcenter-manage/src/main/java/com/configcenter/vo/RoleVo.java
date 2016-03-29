package com.configcenter.vo;

import com.configcenter.model.rbac.Role;

/**
 * 角色vo
 */
public class RoleVo extends Role {

    //是否被选 授权时用到
    private Boolean isSelect=false;

    public Boolean getIsSelect() {
        return isSelect;
    }

    public void setIsSelect(Boolean isSelect) {
        this.isSelect = isSelect;
    }
}
