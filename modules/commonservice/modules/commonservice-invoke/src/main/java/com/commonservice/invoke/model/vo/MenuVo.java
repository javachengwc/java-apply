package com.commonservice.invoke.model.vo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class MenuVo implements Serializable {

    private String name;

    private String icon;

    private String url;

    private String target;

    private List<MenuVo> child;

    public MenuVo() {

    }

    public MenuVo(String name,String icon,String url,String target) {
        this.name = name;
        this.icon = icon;
        this.url = url;
        this.target = target;
    }
}
