package com.commonservice.invoke.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class MetaVo implements Serializable {

    @ApiModelProperty("路由在侧边栏和面包屑中展示的名字")
    private String title;

    @ApiModelProperty("路由的图标，对应路径src/assets/icons/svg")
    private String icon;

    @ApiModelProperty("是否缓存")
    private boolean noCache;

    @ApiModelProperty("内链地址（http(s)://开头）")
    private String link;

    public MetaVo() {

    }

    public MetaVo(String title,String icon,String link) {
        this.title=title;
        this.icon=icon;
        this.link=link;
    }
}
