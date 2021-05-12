package com.commonservice.invoke.model.vo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class MenuVo implements Serializable {

    private String title;

    private String icon;

    private String href;

    private String target;

    private List<MenuVo> child;

    public MenuVo() {

    }

    public MenuVo(String title,String icon,String href,String target) {
        this.title = title;
        this.icon = icon;
        this.href = href;
        this.target = target;
    }
}
