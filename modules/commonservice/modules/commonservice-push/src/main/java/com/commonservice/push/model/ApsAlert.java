package com.commonservice.push.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "apsAlert", description = "ios通知消息载体")
public class ApsAlert {

    @ApiModelProperty(name = "title",value = "标题")
    private String title;

    @ApiModelProperty(name = "subtitle",value = "副标题，一般标题跟副标题只需要一个即可")
    private String subtitle;

    @ApiModelProperty(name = "body",value = "内容")
    private String body;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
