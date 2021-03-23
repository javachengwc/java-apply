package com.commonservice.invoke.model;

import java.io.Serializable;
import lombok.Data;

/**
 * 调用信息
 */
@Data
public class InvokeVo implements Serializable {

    /**
     * 资源链接
     */
    private String resourceLink;

}
