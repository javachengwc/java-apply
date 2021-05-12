package com.commonservice.invoke.model.vo;

import java.io.Serializable;
import lombok.Data;

@Data
public class LogoVo implements Serializable {

    private String title;

    private String image;

    private String href;

    public LogoVo() {

    }

    public LogoVo(String title,String image,String href) {
        this.title=title;
        this.image=image;
        this.href=href;
    }

}
