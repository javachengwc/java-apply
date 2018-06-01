package com.commonservice.push.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang.builder.ToStringBuilder;

@ApiModel(value = "umengNotice", description = "通知信息")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UmengNotice {

    @JsonIgnore
    @ApiModelProperty(name = "deviceFlag", required = true,value = "发送的设备标记 1--android  2--iphone")
    private Integer deviceFlag;

    @ApiModelProperty(name = "appkey", required = true,value = "友盟应用唯一标识")
    private String appkey;

    @ApiModelProperty(name = "alias", required = true,value = "10位或者13位均可")
    private String timestamp;

    @ApiModelProperty(name = "type", required = true,value = "消息发送类型,参见NoticeCastEnum")
    private String type;

    @JsonProperty("device_tokens")
    @ApiModelProperty(name = "deviceTokens", value = "友盟消息推送服务对设备的唯一标识，" +
            "Android的device_token是44位字符串，iOS的device_token是64位"+
            "当type=unicast时, 必填, 表示指定的单个设备,"+"" +
            "当type=listcast时, 必填, 要求不超过500个, 以英文逗号分隔")
    private String deviceTokens;

    @ApiModelProperty(name = "alias", value = "当type=customizedcast时, 选填(此参数和file_id二选一)"+
            "开发者填写自己的alias, 要求不超过500个alias, 多个alias以英文逗号间隔,"+
            "在SDK中调用setAlias(alias, alias_type)时所设置的alias")
    private String alias;

    @JsonProperty("alias_type")
    @ApiModelProperty(name = "aliasType", value = "当type=customizedcast时, 必填"+
            "alias的类型, alias_type可由开发者自定义, " +
            "开发者在SDK中调用setAlias(alias, alias_type)时所设置的alias_type")
    private String aliasType;

    @JsonProperty("file_id")
    @ApiModelProperty(name = "fileId", value = "当type=filecast时，必填，file内容为多条device_token，以回车符分割"+
            "当type=customizedcast时，选填(此参数和alias二选一),file内容为多条alias，以回车符分隔。")
    private String fileId;

    @ApiModelProperty(name = "filter", value = "当type=groupcast时，必填，用户筛选条件，如用户标签、渠道等")
    private String filter;

    @JsonProperty("payload")
    @ApiModelProperty(name = "payload", required = true,value = "消息载体")
    private NoticePayload payload;

    @JsonProperty("policy")
    @ApiModelProperty(name = "policy", value = "发送策略，可选")
    private NoticePolicy policy;

    @JsonProperty("production_mode")
    @ApiModelProperty(name = "productionMode", value = "true/false,可选，正式/测试模式,默认为true。"+
                                           "测试模式只会将消息发给测试设备,测试设备需要到web上添加。")
    private Boolean productionMode;

    @ApiModelProperty(name = "description", value = "发送消息描述")
    private String description;

    @ApiModelProperty(name = "mipush", value = "可选，默认false。当为true时，表示MIUI、EMUI、Flyme系统设备离线转为系统下发")
    private Boolean mipush;

    @ApiModelProperty(name = "mi_activity", value = "可选，mipush为true时生效，表示走系统通道时打开指定页面acitivity的完整包路径")
    @JsonProperty("mi_activity")
    private String miActivity;

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDeviceTokens() {
        return deviceTokens;
    }

    public void setDeviceTokens(String deviceTokens) {
        this.deviceTokens = deviceTokens;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAliasType() {
        return aliasType;
    }

    public void setAliasType(String aliasType) {
        this.aliasType = aliasType;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Boolean getProductionMode() {
        return productionMode;
    }

    public void setProductionMode(Boolean productionMode) {
        this.productionMode = productionMode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public NoticePayload getPayload() {
        return payload;
    }

    public void setPayload(NoticePayload payload) {
        this.payload = payload;
    }

    public NoticePolicy getPolicy() {
        return policy;
    }

    public void setPolicy(NoticePolicy policy) {
        this.policy = policy;
    }

    public Boolean getMipush() {
        return mipush;
    }

    public void setMipush(Boolean mipush) {
        this.mipush = mipush;
    }

    public String getMiActivity() {
        return miActivity;
    }

    public void setMiActivity(String miActivity) {
        this.miActivity = miActivity;
    }

    public Integer getDeviceFlag() {
        return deviceFlag;
    }

    public void setDeviceFlag(Integer deviceFlag) {
        this.deviceFlag = deviceFlag;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
