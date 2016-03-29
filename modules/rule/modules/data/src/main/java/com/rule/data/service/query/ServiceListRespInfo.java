package com.rule.data.service.query;

import com.rule.data.handler.BaseRespInfo;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;
import java.util.Map;

public class ServiceListRespInfo extends BaseRespInfo {

    private List<List<Map<String, String>>> rowsList;

    public List<List<Map<String, String>>> getRowsList() {
        return rowsList;
    }

    public void setRowsList(List<List<Map<String, String>>> rowsList) {
        this.rowsList = rowsList;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
