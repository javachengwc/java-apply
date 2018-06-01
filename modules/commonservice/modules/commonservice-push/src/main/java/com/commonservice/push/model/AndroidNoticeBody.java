package com.commonservice.push.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "androidNoticeBody", description = "android通知消息体")
public class AndroidNoticeBody {

    @ApiModelProperty(name = "ticker",value = "通知栏提示文字，当display_type=notification时必填")
    private String ticker;

    @ApiModelProperty(name = "title",value = "通知标题，当display_type=notification时必填")
    private String title;

    @ApiModelProperty(name = "text",value = "通知文字描述，当display_type=notification时必填")
    private String text;

    @ApiModelProperty(name = "icon",value = "可选，状态栏图标ID")
    private String icon;

    //接口定义里面json节点名就是largeIcon，不需要转换成下划线字段名
    @ApiModelProperty(name = "largeIcon",value = "可选，通知栏拉开后左侧图标ID")
    private String largeIcon;

    @ApiModelProperty(name = "img",value = "可选，通知栏大图标的URL链接")
    private String img;

    @ApiModelProperty(name = "sound",value = "可选，自定义通知声音")
    private String sound;

    @JsonProperty("builder_id")
    @ApiModelProperty(name = "builderId",value = "可选，自定义通知样式")
    private Integer builderId;

    @JsonProperty("play_vibrate")
    @ApiModelProperty(name = "playVibrate",value = "可选，true/false，收到通知是否震动，默认为true")
    private String playVibrate;

    @JsonProperty("play_lights")
    @ApiModelProperty(name = "playLights",value = "可选，true/false，收到通知是否闪灯，默认为true")
    private String playLights;

    @JsonProperty("play_sound")
    @ApiModelProperty(name = "playSound",value = "可选，true/false，收到通知是否发出声音，默认为true")
    private String playSound;

    @JsonProperty("after_open")
    @ApiModelProperty(name = "afterOpen",value = "可选，点击后的后续行为，参见NoticeGoEnum,默认为go_app")
    private String afterOpen;

    @ApiModelProperty(name = "url",value = "当after_open=go_url时需要填写的值")
    private String url;

    @ApiModelProperty(name = "activity",value = "当after_open=go_activity时需要填写的值")
    private String activity;

    @ApiModelProperty(name = "custom",value = "当display_type=message时必填,可以为字符串或者JSON格式,"+
                                        "当display_type=notification且after_open=go_custom时，必填。")
    private String custom;

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(String largeIcon) {
        this.largeIcon = largeIcon;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public Integer getBuilderId() {
        return builderId;
    }

    public void setBuilderId(Integer builderId) {
        this.builderId = builderId;
    }

    public String getPlayVibrate() {
        return playVibrate;
    }

    public void setPlayVibrate(String playVibrate) {
        this.playVibrate = playVibrate;
    }

    public String getPlayLights() {
        return playLights;
    }

    public void setPlayLights(String playLights) {
        this.playLights = playLights;
    }

    public String getPlaySound() {
        return playSound;
    }

    public void setPlaySound(String playSound) {
        this.playSound = playSound;
    }

    public String getAfterOpen() {
        return afterOpen;
    }

    public void setAfterOpen(String afterOpen) {
        this.afterOpen = afterOpen;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
