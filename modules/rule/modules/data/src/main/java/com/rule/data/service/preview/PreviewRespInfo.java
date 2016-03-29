package com.rule.data.service.preview;

import com.rule.data.handler.BaseRespInfo;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PreviewRespInfo extends BaseRespInfo {

    private List<Map<String, String>> rows;

    private ProcessInfo processInfo;

    private List<LinkedHashMap<String,Object>> explains;


    public List<LinkedHashMap<String, Object>> getExplains() {
        return explains;
    }

    public void setExplains(List<LinkedHashMap<String, Object>> explains) {
        this.explains = explains;
    }

    public ProcessInfo getProcessInfo() {
        return processInfo;
    }

    public void setProcessInfo(ProcessInfo processInfo) {
        this.processInfo = processInfo;
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
