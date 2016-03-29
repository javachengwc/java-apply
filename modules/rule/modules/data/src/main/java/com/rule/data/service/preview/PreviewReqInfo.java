package com.rule.data.service.preview;

import com.rule.data.model.SerService;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.Map;

public class PreviewReqInfo {

    /**
     * 1代表用serviceID预览，2代表使用临时ServiceInfo预览
     */
    private Integer previewType = 1;

    private SerService info;

    private Integer serviceID;

    private Integer previewCount = 10;

    private Map<String, Object> param;

    private String orderColumnName = null;

    private String order = "asc";

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderColumnName() {
        return orderColumnName;
    }

    public void setOrderColumnName(String orderColumnName) {
        this.orderColumnName = orderColumnName;
    }

    public Integer getPreviewCount() {
        return previewCount;
    }

    public void setPreviewCount(Integer previewCount) {
        this.previewCount = previewCount;
    }

    public SerService getInfo() {
        return info;
    }

    public void setInfo(SerService info) {
        this.info = info;
    }

    public Integer getPreviewType() {
        return previewType;
    }

    public void setPreviewType(Integer previewType) {
        this.previewType = previewType;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

    public Integer getServiceID() {
        return serviceID;
    }

    public void setServiceID(Integer serviceID) {
        this.serviceID = serviceID;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
