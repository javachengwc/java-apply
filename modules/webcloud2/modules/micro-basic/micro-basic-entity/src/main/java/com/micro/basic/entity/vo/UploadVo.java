package com.micro.basic.entity.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "uploadVo", description = "文件上传")
public class UploadVo {

    @ApiModelProperty("文件名")
    private String fileName;

    @ApiModelProperty("访问url")
    private String url;

    @ApiModelProperty("文件path")
    private String path;

}
