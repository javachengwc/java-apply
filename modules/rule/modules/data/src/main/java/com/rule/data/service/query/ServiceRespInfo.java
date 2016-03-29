package com.rule.data.service.query;

import com.rule.data.handler.BaseRespInfo;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.List;
import java.util.Map;

public class ServiceRespInfo extends BaseRespInfo {

    private List<Map<String, String>> rows;

    private Map<String, List<Map<String, String>>> rowsList;

    public Map<String, List<Map<String, String>>> getRowsList() {
        return rowsList;
    }

    public void setRowsList(Map<String, List<Map<String, String>>> rowsList) {
        this.rowsList = rowsList;
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public void setRows(List<Map<String, String>> rows) {
        this.rows = rows;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
