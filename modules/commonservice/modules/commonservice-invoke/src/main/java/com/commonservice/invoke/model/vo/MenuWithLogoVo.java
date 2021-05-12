package com.commonservice.invoke.model.vo;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
public class MenuWithLogoVo implements Serializable {

    private LogoVo logoInfo;

    private List<MenuVo> menuInfo;

    public MenuWithLogoVo() {

    }
}
