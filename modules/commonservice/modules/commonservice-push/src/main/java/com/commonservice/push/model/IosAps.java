package com.commonservice.push.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "iosAps", description = "ios aps消息体")
public class IosAps {

    @ApiModelProperty(name = "alert",value = "当content-available=1时(静默推送),可选; 否则必填")
    private ApsAlert alert;

    @ApiModelProperty(name = "badge",value = "可选")
    private Integer badge;

    @ApiModelProperty(name = "sound",value = "可选")
    private String sound;

    @JsonProperty("content-available")
    @ApiModelProperty(name = "contentAvailable",value = "可选，默认1，代表静默推送")
    private Integer contentAvailable;

    @ApiModelProperty(name = "category",value = "可选，注意: ios8才支持该字段")
    private String category;

    public ApsAlert getAlert() {
        return alert;
    }

    public void setAlert(ApsAlert alert) {
        this.alert = alert;
    }

    public Integer getBadge() {
        return badge;
    }

    public void setBadge(Integer badge) {
        this.badge = badge;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Integer getContentAvailable() {
        return contentAvailable;
    }

    public void setContentAvailable(Integer contentAvailable) {
        this.contentAvailable = contentAvailable;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
