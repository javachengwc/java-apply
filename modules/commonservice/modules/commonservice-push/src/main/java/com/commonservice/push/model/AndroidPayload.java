package com.commonservice.push.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Map;

@ApiModel(value = "androidPayload", description = "android通知消息载体")
public class AndroidPayload implements NoticePayload{

    @JsonProperty("display_type")
    @ApiModelProperty(name = "displayType",required = true,value = "消息类型,参见NoticeTypeEnum")
    private String displayType;

    @JsonProperty("body")
    @ApiModelProperty(name = "body",required = true,value = "消息体")
    private AndroidNoticeBody body;

    @ApiModelProperty(name = "extra",value = "额外信息")
    private Map<String,Object> extra;

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }

    public AndroidNoticeBody getBody() {
        return body;
    }

    public void setBody(AndroidNoticeBody body) {
        this.body = body;
    }

    public Map<String, Object> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, Object> extra) {
        this.extra = extra;
    }

    public String toString() {

        return ToStringBuilder.reflectionToString(this);
    }
}
